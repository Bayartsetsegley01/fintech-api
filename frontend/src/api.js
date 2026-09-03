const BASE_URL = 'http://localhost:8080/api'

async function request(path, options = {}) {
  const res = await fetch(`${BASE_URL}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  })

  // 204 / empty-body responses (e.g. transfer)
  const text = await res.text()
  const data = text ? JSON.parse(text) : null

  if (!res.ok) {
    const message = (data && (data.message || data.error)) || `Request failed (${res.status})`
    throw new Error(message)
  }
  return data
}

export const api = {
  // Users
  getUsers: () => request('/users'),
  getUser: (id) => request(`/users/${id}`),
  createUser: (payload) => request('/users', { method: 'POST', body: JSON.stringify(payload) }),

  // Accounts
  getAccountsByUser: (userId) => request(`/accounts/user/${userId}`),
  getAccount: (id) => request(`/accounts/${id}`),
  getBalance: (id) => request(`/accounts/${id}/balance`),
  createAccount: (payload) => request('/accounts', { method: 'POST', body: JSON.stringify(payload) }),

  // Transactions
  getTransactions: (accountId) => request(`/transactions/${accountId}`),
  deposit: (payload) => request('/transactions/deposit', { method: 'POST', body: JSON.stringify(payload) }),
  withdraw: (payload) => request('/transactions/withdraw', { method: 'POST', body: JSON.stringify(payload) }),
  transfer: (payload) => request('/transactions/transfer', { method: 'POST', body: JSON.stringify(payload) })
}
