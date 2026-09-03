import { useState } from 'react'
import { Button, Field, Input, EmptyState } from './ui.jsx'
import { initials } from '../format.js'

export default function UsersPanel({ users, selectedUserId, onSelect, onCreate, loading }) {
  const [showForm, setShowForm] = useState(false)
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [submitting, setSubmitting] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    if (!name.trim() || !email.trim()) return
    setSubmitting(true)
    try {
      await onCreate({ name: name.trim(), email: email.trim() })
      setName('')
      setEmail('')
      setShowForm(false)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <aside className="flex flex-col h-full border-r border-line w-full md:w-72 shrink-0">
      <div className="flex items-center justify-between px-5 py-4 border-b border-line">
        <div>
          <h1 className="text-sm font-semibold tracking-tight">Ledger</h1>
          <p className="text-2xs text-mute mt-0.5">FinTech Transaction API</p>
        </div>
        <Button variant="ghost" className="!px-3 !py-1.5" onClick={() => setShowForm((s) => !s)}>
          {showForm ? 'Болих' : '+ Хэрэглэгч'}
        </Button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="px-5 py-4 border-b border-line space-y-3">
          <Field label="Нэр">
            <Input value={name} onChange={(e) => setName(e.target.value)} placeholder="Бат-Эрдэнэ" required />
          </Field>
          <Field label="Имэйл">
            <Input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="name@example.com"
              required
            />
          </Field>
          <Button type="submit" className="w-full" disabled={submitting}>
            {submitting ? 'Үүсгэж байна…' : 'Үүсгэх'}
          </Button>
        </form>
      )}

      <div className="flex-1 overflow-y-auto">
        {loading && <div className="px-5 py-4 text-xs text-mute">Ачааллаж байна…</div>}

        {!loading && users.length === 0 && (
          <EmptyState title="Хэрэглэгч алга" hint="Эхлээд хэрэглэгч үүсгэнэ үү." />
        )}

        <ul>
          {users.map((u) => {
            const active = u.id === selectedUserId
            return (
              <li key={u.id}>
                <button
                  onClick={() => onSelect(u.id)}
                  className={`w-full flex items-center gap-3 px-5 py-3 text-left border-b border-line transition-colors ${
                    active ? 'bg-ink text-paper' : 'hover:bg-surface'
                  }`}
                >
                  <span
                    className={`flex items-center justify-center w-8 h-8 shrink-0 text-2xs font-semibold border ${
                      active ? 'border-paper text-paper' : 'border-line text-ink'
                    }`}
                  >
                    {initials(u.name) || '·'}
                  </span>
                  <span className="min-w-0">
                    <span className="block text-sm font-medium truncate">{u.name}</span>
                    <span className={`block text-2xs truncate ${active ? 'text-paper/70' : 'text-mute'}`}>
                      {u.email}
                    </span>
                  </span>
                </button>
              </li>
            )
          })}
        </ul>
      </div>
    </aside>
  )
}
