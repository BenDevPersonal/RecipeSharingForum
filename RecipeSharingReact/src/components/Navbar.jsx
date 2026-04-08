import { Link } from "react-router-dom";

export function Navbar() {
  return (
    <nav className="sticky top-0 bg-white/80 backdrop-blur border-b z-50">
      <div className="max-w-6xl mx-auto flex justify-between items-center px-6 py-4">
        <h2 className="text-xl font-semibold text-gray-800">
          🍳 <span className="text-accent">RecipeForum</span>
        </h2>

        <div className="flex gap-6 text-sm font-medium">
          <Link
            to="/"
            className="hover:text-accent transition"
          >
            Home
          </Link>
        </div>
      </div>
    </nav>
  );
}