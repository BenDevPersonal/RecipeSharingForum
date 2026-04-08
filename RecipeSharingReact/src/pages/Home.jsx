import { useContext, useState } from "react";
import { AppContext } from "../App";
import { useQuery } from "@tanstack/react-query";
import { getCategories } from "../api/categories";
import { getRecipes } from "../api/recipes";

export function Home() {
  const { user } = useContext(AppContext);
  const [selectedCategory, setSelectedCategory] = useState(null);

  const { data: categories, isLoading: catLoading } = useQuery({
    queryKey: ["categories"],
    queryFn: getCategories,
  });

  const { data: recipes, isLoading: recLoading } = useQuery({
    queryKey: ["recipes", selectedCategory],
    queryFn: () => getRecipes(selectedCategory),
  });

  return (
  <div className="px-6 py-10 max-w-6xl mx-auto">
    {/* HERO */}
    <div className="text-center mb-14">
      <h1 className="text-5xl font-bold mb-4 tracking-tight">
        🍳 Discover & Share Recipes
      </h1>

      <p className="text-gray-500 text-lg">
        Join a community of food lovers and explore amazing dishes
      </p>

      <p className="mt-3 text-accent font-medium">
        {user !== "None"
          ? `Welcome back, ${user}!`
          : "Start sharing your recipes today"}
      </p>
    </div>

    {/* CATEGORIES */}
    <div className="mb-12">
      <h2 className="text-2xl font-semibold mb-4">
        Categories
      </h2>

      <div className="flex flex-wrap gap-3">
        {categories?.map((cat) => (
          <button
            key={cat.id}
            onClick={() => setSelectedCategory(cat.id)}
            className={`px-5 py-2 rounded-full text-sm font-medium border transition-all
              ${
                selectedCategory === cat.id
                  ? "bg-accent text-white shadow-soft"
                  : "bg-white hover:bg-accent hover:text-white hover:shadow-soft"
              }`}
          >
            {cat.name}
          </button>
        ))}
      </div>
    </div>

    {/* RECIPES */}
    <div>
      <h2 className="text-2xl font-semibold mb-6">
        Latest Recipes
      </h2>

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
        {recipes?.map((recipe) => (
          <div
            key={recipe.id}
            className="bg-white rounded-2xl overflow-hidden shadow-soft hover:shadow-xl transition-all duration-300 cursor-pointer group"
          >
            {/* IMAGE */}
            <div className="overflow-hidden">
              <img
                src={recipe.imageUrl}
                alt={recipe.title}
                className="h-44 w-full object-cover group-hover:scale-105 transition duration-300"
              />
            </div>

            {/* CONTENT */}
            <div className="p-4">
              <h3 className="font-semibold text-lg group-hover:text-accent transition">
                {recipe.title}
              </h3>

              <p className="text-sm text-gray-500 mt-1">
                {recipe.category?.name}
              </p>
            </div>
          </div>
        ))}
      </div>
    </div>
  </div>
);
}