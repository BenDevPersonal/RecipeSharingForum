import { Link, useNavigate } from "react-router-dom";
import { useTheme } from "./ThemeProvider";
import { useAuth } from "../context/useAuth";
import { useEffect, useRef, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { getCategories } from "../api/categories";
import { getAllergies } from "../api/allergies";
import { getMe } from "../api/users";

export function Navbar() {
    const { toggleTheme, darkMode } = useTheme();
    const { isAuth, logout } = useAuth();
    const navigate = useNavigate();

    const { data: me } = useQuery({
        queryKey: ["me"],
        queryFn: getMe,
        enabled: isAuth,
    });

    const canAccessAdmin = isAuth && ["admin", "manager"].includes(me?.role);

    const [query, setQuery] = useState("");
    const [showFilters, setShowFilters] = useState(false);

    const [mode, setMode] = useState("all");
    const [categories, setCategories] = useState([]);
    const [allergies, setAllergies] = useState([]);

    const filterRef = useRef(null);

    const { data: apiCategories = [] } = useQuery({
        queryKey: ["categories"],
        queryFn: getCategories,
    });

    const { data: apiAllergies = [] } = useQuery({
        queryKey: ["allergies"],
        queryFn: getAllergies,
    });

    function handleSearch() {
        if (!query.trim()) return;

        navigate(
            `/search?q=${query}&mode=${mode}&category=${categories.join(",")}&allergy=${allergies.join(",")}`
        );

        setShowFilters(false);
    }

    function toggleMulti(setter, list, value) {
        setter(
            list.includes(value)
                ? list.filter((v) => v !== value)
                : [...list, value]
        );
    }

    useEffect(() => {
        function handleKey(e) {
            if (e.key === "Escape") setShowFilters(false);
        }

        function handleClickOutside(e) {
            if (filterRef.current && !filterRef.current.contains(e.target)) {
                setShowFilters(false);
            }
        }

        document.addEventListener("keydown", handleKey);
        document.addEventListener("mousedown", handleClickOutside);

        return () => {
            document.removeEventListener("keydown", handleKey);
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    return (
        <nav className="sticky top-0 bg-white/80 dark:bg-gray-900/80 backdrop-blur border-b border-gray-200 dark:border-gray-800 z-50">
            <div className="max-w-6xl mx-auto flex justify-between items-center px-6 py-4">

                <h2 className="text-xl font-semibold text-gray-800 dark:text-gray-100">
                    🍳 <span className="text-accent">RecipeForum</span>
                </h2>

                <div className="relative" ref={filterRef}>

                    <input
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        onKeyDown={(e) => e.key === "Enter" && handleSearch()}
                        placeholder="Search..."
                        className="px-3 py-2 pr-10 rounded-full bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700"
                    />

                    <button
                        onClick={() => setShowFilters(!showFilters)}
                        className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-accent transition"
                    >
                        ⚙️
                    </button>

                    {(categories.length > 0 || allergies.length > 0 || mode !== "all") && (
                        <div className="absolute left-0 top-12 w-full">
                            <div className="flex flex-wrap gap-2 mt-2 p-2 rounded-xl bg-white/70 dark:bg-gray-900/70 backdrop-blur border border-gray-200 dark:border-gray-700 shadow-soft">

                                {mode !== "all" && (
                                    <span className="px-2 py-1 text-xs rounded-full bg-accent text-white">
                                        {mode}
                                    </span>
                                )}

                                {categories.map((c) => (
                                    <span
                                        key={c}
                                        className="px-2 py-1 text-xs rounded-full bg-green-500 text-white"
                                    >
                                        {c}
                                    </span>
                                ))}

                                {allergies.map((a) => (
                                    <span
                                        key={a}
                                        className="px-2 py-1 text-xs rounded-full bg-red-500 text-white"
                                    >
                                        no {a}
                                    </span>
                                ))}

                            </div>
                        </div>
                    )}

                    <div
                        className={`absolute top-14 left-1/2 -translate-x-1/2 w-80 bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-700 rounded-2xl shadow-soft p-4 space-y-4 z-50 transition-all duration-200 origin-top ${showFilters
                            ? "opacity-100 scale-100"
                            : "opacity-0 scale-95 pointer-events-none"
                            }`}
                    >

                        <div>
                            <p className="text-xs font-semibold mb-2">Search type</p>
                            <div className="flex gap-2">
                                {["all", "posts", "users"].map((m) => (
                                    <button
                                        key={m}
                                        onClick={() => setMode(m)}
                                        className={`px-3 py-1 rounded-full text-xs border ${mode === m
                                            ? "bg-accent text-white"
                                            : "bg-gray-100 dark:bg-gray-800"
                                            }`}
                                    >
                                        {m}
                                    </button>
                                ))}
                            </div>
                        </div>

                        <div>
                            <p className="text-xs font-semibold mb-2">Categories</p>
                            <div className="flex flex-wrap gap-2">
                                {apiCategories.map((c) => (
                                    <button
                                        key={c.id}
                                        onClick={() => toggleMulti(setCategories, categories, c.name)}
                                        className={`px-3 py-1 rounded-full text-xs border ${categories.includes(c.name)
                                            ? "bg-accent text-white"
                                            : "bg-gray-100 dark:bg-gray-800"
                                            }`}
                                    >
                                        {c.name}
                                    </button>
                                ))}
                            </div>
                        </div>

                        <div>
                            <p className="text-xs font-semibold mb-2">Exclude allergies</p>
                            <div className="flex flex-wrap gap-2">
                                {apiAllergies.map((a) => (
                                    <button
                                        key={a.id}
                                        onClick={() => toggleMulti(setAllergies, allergies, a.name)}
                                        className={`px-3 py-1 rounded-full text-xs border ${allergies.includes(a.name)
                                            ? "bg-red-500 text-white"
                                            : "bg-gray-100 dark:bg-gray-800"
                                            }`}
                                    >
                                        {a.name}
                                    </button>
                                ))}
                            </div>
                        </div>

                    </div>
                </div>

                <div className="flex items-center gap-6 text-sm font-medium">
                    <Link to="/" className="hover:text-accent transition">Home</Link>
                    <Link to="/profile" className="hover:text-accent transition">Profile</Link>

                    {canAccessAdmin && (
                        <Link
                            to="/admin"
                            className="hover:text-accent transition"
                        >
                            Admin
                        </Link>
                    )}

                    {!isAuth && (
                        <div className="flex items-center gap-4">
                            <Link to="/register" className="hover:text-accent transition">
                                Register
                            </Link>
                            <Link to="/login" className="hover:text-accent transition">
                                Login
                            </Link>
                        </div>
                    )}

                    <ThemeToggle darkMode={darkMode} toggleTheme={toggleTheme} className="hidden sm:flex" />

                    {isAuth && (
                        <button onClick={logout} className="text-red-500 hover:underline">
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