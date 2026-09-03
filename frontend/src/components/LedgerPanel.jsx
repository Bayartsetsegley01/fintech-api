import { useState } from 'react'
import { Button, Field, Input, Select, EmptyState, Pill } from './ui.jsx'
import { formatMoney, formatDate } from '../format.js'

const TABS = [
  { id: 'deposit', label: 'Мөнгө хийх' },
  { id: 'withdraw', label: 'Мөнгө татах' },
  { id: 'transfer', label: 'Шилжүүлэх' }
]

const TYPE_LABEL = {
  DEPOSIT: 'Орлого',
  WITHDRAW: 'Зарлага',
  TRANSFER: 'Шилжүүлэг'
}

export default function LedgerPanel({ account, transactions, allAccounts, loading, onDeposit, onWithdraw, onTransfer }) {
  const [tab, setTab] = useState(null)
  const [amount, setAmount] = useState('')
  const [description, setDescription] = useState('')
  const [toAccountId, setToAccountId] = useState('')
  const [submitting, setSubmitting] = useState(false)

  if (!account) {
    return (
      <section className="flex-1">
        <EmptyState title="Данс сонгоогүй" hint="Гүйлгээ хийхийн тулд данс сонгоно уу." />
      </section>
    )
  }

  function resetForm() {
    setAmount('')
    setDescription('')
    setToAccountId('')
    setTab(null)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    const value = Number(amount)
    if (!value || value <= 0) return

    setSubmitting(true)
    try {
      if (tab === 'deposit') {
        await onDeposit({ accountId: account.id, amount: value, description: description || 'Deposit' })
      } else if (tab === 'withdraw') {
        await onWithdraw({ accountId: account.id, amount: value, description: description || 'Withdrawal' })
      } else if (tab === 'transfer') {
        if (!toAccountId) return
        await onTransfer({
          fromAccountId: account.id,
          toAccountId: Number(toAccountId),
          amount: value,
          description: description || 'Transfer'
        })
      }
      resetForm()
    } finally {
      setSubmitting(false)
    }
  }

  const transferTargets = allAccounts.filter((a) => a.id !== account.id)

  return (
    <section className="flex-1 flex flex-col h-full min-w-0">
      {/* Hero balance */}
      <div className="px-8 py-8 border-b border-line">
        <p className="text-2xs uppercase text-mute mb-2">Дансны дугаар № {account.accountNumber}</p>
        <p className="num text-4xl md:text-5xl font-semibold tracking-tight leading-none">
          {formatMoney(account.balance)}
        </p>
        <p className="text-xs text-mute mt-3">{account.user?.name}</p>

        <div className="flex gap-2 mt-6">
          {TABS.map((t) => (
            <Button
              key={t.id}
              variant={tab === t.id ? 'primary' : 'ghost'}
              onClick={() => setTab(tab === t.id ? null : t.id)}
            >
              {t.label}
            </Button>
          ))}
        </div>

        {tab && (
          <form onSubmit={handleSubmit} className="mt-6 grid grid-cols-1 md:grid-cols-3 gap-4 items-end max-w-2xl">
            <Field label="Дүн">
              <Input
                type="number"
                min="0.01"
                step="0.01"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                placeholder="0"
                autoFocus
                required
              />
            </Field>

            {tab === 'transfer' && (
              <Field label="Хүлээн авах данс">
                <Select value={toAccountId} onChange={(e) => setToAccountId(e.target.value)} required>
                  <option value="" disabled>
                    Данс сонгох
                  </option>
                  {transferTargets.map((a) => (
                    <option key={a.id} value={a.id}>
                      № {a.accountNumber} — {a.user?.name}
                    </option>
                  ))}
                </Select>
              </Field>
            )}

            <Field label="Тайлбар (заавал биш)">
              <Input value={description} onChange={(e) => setDescription(e.target.value)} placeholder="Тэмдэглэл" />
            </Field>

            <div className="md:col-span-3 flex gap-3">
              <Button type="submit" disabled={submitting}>
                {submitting ? 'Илгээж байна…' : 'Баталгаажуулах'}
              </Button>
              <Button type="button" variant="subtle" onClick={resetForm}>
                Цуцлах
              </Button>
            </div>
          </form>
        )}
      </div>

      {/* Transaction history */}
      <div className="flex-1 overflow-y-auto">
        <div className="px-8 pt-6 pb-2">
          <h3 className="text-2xs uppercase text-mute">Гүйлгээний түүх</h3>
        </div>

        {loading && <div className="px-8 py-4 text-xs text-mute">Ачааллаж байна…</div>}

        {!loading && transactions.length === 0 && (
          <EmptyState title="Гүйлгээ алга" hint="Эхний гүйлгээгээ дээрх товчоор хийнэ үү." />
        )}

        {!loading && transactions.length > 0 && (
          <table className="w-full text-sm">
            <thead>
              <tr className="border-y border-line text-2xs uppercase text-mute">
                <th className="text-left font-medium px-8 py-2.5">Огноо</th>
                <th className="text-left font-medium px-2 py-2.5">Төрөл</th>
                <th className="text-left font-medium px-2 py-2.5">Тайлбар</th>
                <th className="text-right font-medium px-8 py-2.5">Дүн</th>
              </tr>
            </thead>
            <tbody>
              {[...transactions]
                .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
                .map((t) => (
                  <tr key={t.id} className="border-b border-line">
                    <td className="px-8 py-3 text-xs text-mute whitespace-nowrap">{formatDate(t.createdAt)}</td>
                    <td className="px-2 py-3">
                      <Pill>{TYPE_LABEL[t.type] || t.type}</Pill>
                    </td>
                    <td className="px-2 py-3 text-mute truncate max-w-xs">{t.description || '—'}</td>
                    <td className="px-8 py-3 text-right num font-medium">
                      {t.type === 'WITHDRAW' ? '−' : '+'}
                      {formatMoney(t.amount)}
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        )}
      </div>
    </section>
  )
}
