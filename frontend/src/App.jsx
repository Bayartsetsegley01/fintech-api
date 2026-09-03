import { useEffect, useState, useCallback } from 'react'
import { api } from './api.js'
import { ErrorBanner } from './components/ui.jsx'
import UsersPanel from './components/UsersPanel.jsx'
import AccountsPanel from './components/AccountsPanel.jsx'
import LedgerPanel from './components/LedgerPanel.jsx'

export default function App() {
  const [users, setUsers] = useState([])
  const [usersLoading, setUsersLoading] = useState(true)
  const [selectedUserId, setSelectedUserId] = useState(null)

  const [accountsByUser, setAccountsByUser] = useState({})
  const [accountsLoading, setAccountsLoading] = useState(false)
  const [selectedAccountId, setSelectedAccountId] = useState(null)

  const [transactions, setTransactions] = useState([])
  const [transactionsLoading, setTransactionsLoading] = useState(false)

  const [error, setError] = useState('')

  const fail = (err) => setError(err.message || 'Тодорхойгүй алдаа гарлаа')

  // Load users on first render
  useEffect(() => {
    ;(async () => {
      try {
        const data = await api.getUsers()
        setUsers(data)
        if (data.length > 0) setSelectedUserId(data[0].id)
      } catch (e) {
        fail(e)
      } finally {
        setUsersLoading(false)
      }
    })()
  }, [])

  const loadAccountsForUser = useCallback(async (userId) => {
    setAccountsLoading(true)
    try {
      const data = await api.getAccountsByUser(userId)
      setAccountsByUser((prev) => ({ ...prev, [userId]: data }))
      return data
    } catch (e) {
      fail(e)
      return []
    } finally {
      setAccountsLoading(false)
    }
  }, [])

  // Load accounts whenever the selected user changes
  useEffect(() => {
    if (!selectedUserId) return
    setSelectedAccountId(null)
    setTransactions([])
    ;(async () => {
      const data = await loadAccountsForUser(selectedUserId)
      if (data.length > 0) setSelectedAccountId(data[0].id)
    })()
  }, [selectedUserId, loadAccountsForUser])

  // Warm the account cache for every user, so transfer target lists are complete
  useEffect(() => {
    users.forEach((u) => {
      if (!accountsByUser[u.id]) loadAccountsForUser(u.id)
    })
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [users])

  const loadTransactions = useCallback(async (accountId) => {
    setTransactionsLoading(true)
    try {
      const data = await api.getTransactions(accountId)
      setTransactions(data)
    } catch (e) {
      fail(e)
    } finally {
      setTransactionsLoading(false)
    }
  }, [])

  useEffect(() => {
    if (!selectedAccountId) {
      setTransactions([])
      return
    }
    loadTransactions(selectedAccountId)
  }, [selectedAccountId, loadTransactions])

  async function handleCreateUser(payload) {
    try {
      const user = await api.createUser(payload)
      setUsers((prev) => [...prev, user])
      setSelectedUserId(user.id)
    } catch (e) {
      fail(e)
      throw e
    }
  }

  async function handleCreateAccount(payload) {
    try {
      const account = await api.createAccount(payload)
      await loadAccountsForUser(payload.userId)
      setSelectedAccountId(account.id)
    } catch (e) {
      fail(e)
      throw e
    }
  }

  async function refreshAfterTransaction(accountId) {
    await Promise.all([loadAccountsForUser(selectedUserId), loadTransactions(accountId)])
  }

  async function handleDeposit(payload) {
    try {
      await api.deposit(payload)
      await refreshAfterTransaction(payload.accountId)
    } catch (e) {
      fail(e)
      throw e
    }
  }

  async function handleWithdraw(payload) {
    try {
      await api.withdraw(payload)
      await refreshAfterTransaction(payload.accountId)
    } catch (e) {
      fail(e)
      throw e
    }
  }

  async function handleTransfer(payload) {
    try {
      await api.transfer(payload)
      // Refresh accounts for every user we've cached, since the receiving
      // account may belong to someone else
      await Promise.all(Object.keys(accountsByUser).map((uid) => loadAccountsForUser(Number(uid))))
      await loadTransactions(payload.fromAccountId)
    } catch (e) {
      fail(e)
      throw e
    }
  }

  const selectedUser = users.find((u) => u.id === selectedUserId) || null
  const accounts = selectedUserId ? accountsByUser[selectedUserId] || [] : []
  const selectedAccount = accounts.find((a) => a.id === selectedAccountId) || null

  const allAccounts = Object.values(accountsByUser).flat()

  return (
    <div className="h-screen w-screen flex flex-col overflow-hidden">
      {error && (
        <div className="px-5 py-2">
          <ErrorBanner message={error} onDismiss={() => setError('')} />
        </div>
      )}

      <div className="flex-1 flex overflow-hidden">
        <UsersPanel
          users={users}
          selectedUserId={selectedUserId}
          onSelect={setSelectedUserId}
          onCreate={handleCreateUser}
          loading={usersLoading}
        />

        <AccountsPanel
          user={selectedUser}
          accounts={accounts}
          selectedAccountId={selectedAccountId}
          onSelect={setSelectedAccountId}
          onCreate={handleCreateAccount}
          loading={accountsLoading}
        />

        <LedgerPanel
          account={selectedAccount}
          transactions={transactions}
          allAccounts={allAccounts}
          loading={transactionsLoading}
          onDeposit={handleDeposit}
          onWithdraw={handleWithdraw}
          onTransfer={handleTransfer}
        />
      </div>
    </div>
  )
}
