import { Link } from "react-router-dom";
import { useTheme } from "./ThemeProvider";
import { useAuth } from "../context/AuthContext";

export function Navbar() {
    const { toggleTheme, darkMode } = useTheme();
    const { isAuth, logout } = useAuth();

    return (
        <nav className="sticky top-0 bg-white/80 dark:bg-gray-900/80 backdrop-blur border-b border-gray-200 dark:border-gray-800 z-50">
            <div className="max-w-6xl mx-auto px-6 py-4 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">

                <div className="flex justify-between items-center">
                    <h2 className="text-xl font-semibold text-gray-800 dark:text-gray-100">
                        🍳 <span className="text-accent">RecipeForum</span>
                    </h2>

                    <ThemeToggle darkMode={darkMode} toggleTheme={toggleTheme} className="sm:hidden" />
                </div>

                <div className="flex justify-center sm:justify-end items-center gap-6 text-sm font-medium">
                    <Link to="/" className="hover:text-accent transition">
                        Home
                    </Link>

                    <Link to="/profile" className="hover:text-accent transition">
                        Profile
                    </Link>

                    <ThemeToggle darkMode={darkMode} toggleTheme={toggleTheme} className="hidden sm:flex" />

                    {isAuth && (
                        <button
                            onClick={logout}
                            className="text-red-500 hover:underline"
                        >
                            Logout
                        </button>
                    )}
                </div>

            </div>
        </nav>
    );
}

function ThemeToggle({ darkMode, toggleTheme, className }) {
    return (
        <button
            onClick={toggleTheme}
            className={`relative inline-flex items-center w-14 h-7 rounded-full transition-colors duration-300 ${darkMode ? "bg-accent" : "bg-gray-300"
                } ${className}`}
        >
            <span
                className={`absolute left-1 top-1 w-5 h-5 rounded-full bg-white shadow-md transform transition-transform duration-300 flex items-center justify-center text-xs ${darkMode ? "translate-x-7" : ""
                    }`}
            >
                {darkMode ? "🌙" : "☀️"}
            </span>
        </button>
    );
}