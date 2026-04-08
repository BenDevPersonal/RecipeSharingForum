/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx}",
  ],
  theme: {
    extend: {
  colors: {
    accent: "#8b5cf6", // softer purple
    accentDark: "#7c3aed",
  },
  boxShadow: {
    soft: "0 10px 25px rgba(0,0,0,0.05)",
  },
},
  },
  plugins: [],
};