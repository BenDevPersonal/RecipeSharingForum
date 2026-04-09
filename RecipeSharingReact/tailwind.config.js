/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: [
    "./index.html",
    "./src/**/*.{js,jsx}",
  ],
  theme: {
    extend: {
      colors: {
        accent: "#f68d5c",
        accentDark: "#ed643a",
      },
      boxShadow: {
        soft: "0 10px 25px rgba(0,0,0,0.05)",
      },
    },
  },
  plugins: [],
};