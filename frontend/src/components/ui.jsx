export function Button({ children, variant = 'primary', className = '', ...props }) {
  const base = 'inline-flex items-center justify-center px-4 py-2 text-sm font-medium transition-colors duration-150 disabled:opacity-40 disabled:cursor-not-allowed'
  const variants = {
    primary: 'bg-ink text-paper hover:bg-black',
    ghost: 'bg-transparent text-ink border border-line hover:border-ink',
    subtle: 'bg-transparent text-mute hover:text-ink'
  }
  return (
    <button className={`${base} ${variants[variant]} ${className}`} {...props}>
      {children}
    </button>
  )
}

export function Field({ label, children }) {
  return (
    <label className="block">
      <span className="block text-2xs uppercase text-mute mb-1.5">{label}</span>
      {children}
    </label>
  )
}

export function Input(props) {
  return (
    <input
      className="w-full border border-line bg-paper px-3 py-2 text-sm text-ink placeholder:text-mute focus:border-ink transition-colors"
      {...props}
    />
  )
}

export function Select({ children, ...props }) {
  return (
    <select
      className="w-full border border-line bg-paper px-3 py-2 text-sm text-ink focus:border-ink transition-colors"
      {...props}
    >
      {children}
    </select>
  )
}

export function ErrorBanner({ message, onDismiss }) {
  if (!message) return null
  return (
    <div className="flex items-start justify-between gap-3 border border-ink bg-ink text-paper px-3 py-2 text-sm">
      <span>{message}</span>
      <button onClick={onDismiss} className="text-paper/70 hover:text-paper leading-none text-base">
        ×
      </button>
    </div>
  )
}

export function EmptyState({ title, hint }) {
  return (
    <div className="flex flex-col items-center justify-center text-center py-16 px-6">
      <p className="text-sm text-ink">{title}</p>
      {hint && <p className="text-xs text-mute mt-1 max-w-xs">{hint}</p>}
    </div>
  )
}

export function Pill({ children }) {
  return (
    <span className="inline-block border border-line px-2 py-0.5 text-2xs uppercase text-mute">
      {children}
    </span>
  )
}
