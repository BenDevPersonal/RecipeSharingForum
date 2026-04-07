import { useState, createContext } from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import './App.css'
import { Home } from './pages/Home'
import { Navbar } from './components/Navbar'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'

export const AppContext = createContext();

function App() {
  const [user, setUser] = useState("None");

  const client = new QueryClient({
    defaultOptions: {
      queries: {
        refetchOnWindowFocus: false
      }
    }
  });

  return (
    <AppContext.Provider value={ {user, setUser} }>
      <QueryClientProvider client={client}>
        <div>
          <Router>
            <Navbar/>
            <Routes>
              <Route path='/' element={<Home />} />
              <Route path='/*' element={<>Page Not Found</>} />
            </Routes>
          </Router>
        </div>
      </QueryClientProvider>
    </AppContext.Provider>
  )
}

export default App
