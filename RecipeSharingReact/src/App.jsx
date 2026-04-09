import { useState, createContext } from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { Home } from './pages/Home'
import { Navbar } from './components/Navbar'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ThemeProvider } from './components/ThemeProvider'

export const AppContext = createContext();

const client = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false
    }
  }
});

function App() {
  const [user, setUser] = useState("None");

  return (
    <AppContext.Provider value={{ user, setUser }}>
      <QueryClientProvider client={client}>
        <ThemeProvider>
          <Router>
            <Navbar />
            <Routes>
              <Route path='/' element={<Home />} />
              <Route path='/*' element={<>Page Not Found</>} />
            </Routes>
          </Router>
        </ThemeProvider>
      </QueryClientProvider>
    </AppContext.Provider>
  )
}

export default App;