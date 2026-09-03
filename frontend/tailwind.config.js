/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Montserrat', 'ui-sans-serif', 'system-ui', 'sans-serif']
      },
      colors: {
        ink: '#0A0A0A',
        paper: '#FFFFFF',
        line: '#E5E5E5',
        mute: '#8A8A8A',
        surface: '#FAFAFA'
      },
      fontSize: {
        '2xs': ['0.6875rem', { letterSpacing: '0.04em' }]
      }
    }
  },
  plugins: []
}
