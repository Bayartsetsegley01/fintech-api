import { useState } from 'react'
import { Button, Field, Input, EmptyState } from './ui.jsx'
import { formatMoney } from '../format.js'

export default function AccountsPanel({ user, accounts, selectedAccountId, onSelect, onCreate, loading }) {
  const [showForm, setShowForm] = useState(false)
  const [accountNumber, setAccountNumber] = useState('')
  const [initialBalance, setInitialBalance] = useState('')
  const [submitting, setSubmitting] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    if (!accountNumber.trim()) return
    setSubmitting(true)
    try {
      await onCreate({
        userId: user.id,
        accountNumber: accountNumber.trim(),
        initialBalance: initialBalance === '' ? 0 : Number(initialBalance)
      })
      setAccountNumber('')
      setInitialBalance('')
      setShowForm(false)
    } finally {
      setSubmitting(false)
    }
  }

  if (!user) {
    return (
      <section className="w-full md:w-80 shrink-0 border-r border-line">
        <EmptyState title="Хэрэглэгч сонгоогүй" hint="Зүүн талаас хэрэглэгч сонгоно уу." />
      </section>
    )
  }

  return (
    <section className="flex flex-col h-full border-r border-line w-full md:w-80 shrink-0">
      <div className="flex items-center justify-between px-5 py-4 border-b border-line">
        <div className="min-w-0">
          <h2 className="text-sm font-semibold truncate">{user.name}</h2>
          <p className="text-2xs text-mute truncate">{user.email}</p>
        </div>
        <Button variant="ghost" className="!px-3 !py-1.5 shrink-0" onClick={() => setShowForm((s) => !s)}>
          {showForm ? 'Болих' : '+ Данс'}
        </Button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="px-5 py-4 border-b border-line space-y-3">
          <Field label="Дансны дугаар">
            <Input
              value={accountNumber}
              onChange={(e) => setAccountNumber(e.target.value)}
              placeholder="10000001"
              required
            />
          </Field>
          <Field label="Эхний үлдэгдэл">
            <Input
              type="number"
              min="0"
              step="0.01"
              value={initialBalance}
              onChange={(e) => setInitialBalance(e.target.value)}
              placeholder="0"
            />
          </Field>
          <Button type="submit" className="w-full" disabled={submitting}>
            {submitting ? 'Үүсгэж байна…' : 'Данс үүсгэх'}
          </Button>
        </form>
      )}

      <div className="flex-1 overflow-y-auto">
        {loading && <div className="px-5 py-4 text-xs text-mute">Ачааллаж байна…</div>}

        {!loading && accounts.length === 0 && (
          <EmptyState title="Данс алга" hint="Энэ хэрэглэгчид данс нэмнэ үү." />
        )}

        <ul>
          {accounts.map((a) => {
            const active = a.id === selectedAccountId
            return (
              <li key={a.id}>
                <button
                  onClick={() => onSelect(a.id)}
                  className={`w-full flex flex-col gap-1 px-5 py-3.5 text-left border-b border-line transition-colors ${
                    active ? 'bg-ink text-paper' : 'hover:bg-surface'
                  }`}
                >
                  <span className={`text-2xs uppercase tracking-wide ${active ? 'text-paper/70' : 'text-mute'}`}>
                    № {a.accountNumber}
                  </span>
                  <span className="num text-base font-semibold">{formatMoney(a.balance)}</span>
                </button>
              </li>
            )
          })}
        </ul>
      </div>
    </section>
  )
}
