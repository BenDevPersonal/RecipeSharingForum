import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import { Home } from "./pages/Home";
import { Profile } from "./pages/Profile";
import { Post } from "./pages/Post";
import { Search } from "./pages/Search";
import { User } from "./pages/User";
import { Register } from "./pages/Register";
import { Login } from "./pages/Login";
import { AdminDashboard } from "./pages/AdminDashboard";
import { AdminRoute } from "./components/AdminRoute";
import { Navbar } from './components/Navbar'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ThemeProvider } from './components/ThemeProvider'
import { ProtectedRoute } from './components/ProtectedRoute'
import { CreatePost } from "./pages/CreatePost";
import { EditPost } from "./pages/EditPost";
import { AuthProvider } from "./context/AuthProvider";

const client = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
    },
  },
});

function App() {
  return (
    <AppContext.Provider value={{ user, setUser }}>
    </AppContext.Provider>
    <QueryClientProvider client={client}>
      <AuthProvider>
        <ThemeProvider>
          <Router>
            <Navbar />

            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/post/:id" element={<Post />} />
              <Route path="/search" element={<Search />} />
              <Route path="/user/:id" element={<User />} />

              <Route
                path="/create"
                element={
                  <ProtectedRoute>
                    <CreatePost />
                  </ProtectedRoute>
                }
              />

              <Route
                path="/posts/edit/:id"
                element={
                  <ProtectedRoute>
                    <EditPost />
                  </ProtectedRoute>
                }
              />

              <Route
                path="/profile"
                element={
                  <ProtectedRoute>
                    <Profile />
                  </ProtectedRoute>
                }
              />

              <Route path="/register" element={<Register />} />
              <Route path="/login" element={<Login />} />
                
              <Route path="/admin" element={<AdminRoute> <AdminDashboard /> </AdminRoute>} />

              <Route path="/*" element={<>Page Not Found</>} />
            </Routes>
          </Router>
        </ThemeProvider>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;