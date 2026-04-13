import { useState, createContext } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import { Home } from "./pages/Home";
import { Profile } from "./pages/Profile";
import { Post } from "./pages/Post";
import { Search } from "./pages/Search";
import { User } from "./pages/User";
import { Register } from "./pages/Register";
import { Login } from "./pages/Login";
import { CreatePost } from "./pages/CreatePost";

import { Navbar } from "./components/Navbar";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ThemeProvider } from "./components/ThemeProvider";
import { AuthProvider } from "./context/AuthContext";
import { ProtectedRoute } from "./components/ProtectedRoute";

export const AppContext = createContext();

const client = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
    },
  },
});

function App() {
  const [user, setUser] = useState("None");

  return (
    <AppContext.Provider value={{ user, setUser }}>
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

                <Route path="/profile" element={
                  <ProtectedRoute>
                    <Profile />
                  </ProtectedRoute>
                } />

                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />

                <Route path="/*" element={<>Page Not Found</>} />
              </Routes>

            </Router>
          </ThemeProvider>
        </AuthProvider>
      </QueryClientProvider>
    </AppContext.Provider>
  );
}

export default App;