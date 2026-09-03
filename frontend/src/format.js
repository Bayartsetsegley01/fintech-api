export function formatMoney(value) {
  const n = typeof value === 'string' ? parseFloat(value) : value
  if (n === null || n === undefined || Number.isNaN(n)) return '—'
  return new Intl.NumberFormat('mn-MN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(n) + '₮'
}

export function formatDate(value) {
  if (!value) return '—'
  const d = new Date(value)
  return new Intl.DateTimeFormat('mn-MN', {
    year: 'numeric',
    month: 'short',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).format(d)
}

export function initials(name = '') {
  return name
    .trim()
    .split(/\s+/)
    .slice(0, 2)
    .map((p) => p[0]?.toUpperCase())
    .join('')
}
