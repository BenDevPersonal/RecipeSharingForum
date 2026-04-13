import { useContext, useState } from "react";
import { AppContext } from "../App";
import { useQuery } from "@tanstack/react-query";
import { getCategories } from "../api/categories";
import { getRecipes } from "../api/recipes";
import { useNavigate } from "react-router-dom";
import { ErrorMessage } from "../components/ErrorMessage";
import { useAuth } from "../context/AuthContext";

export function Home() {
  const { user } = useContext(AppContext);
  const { isAuth } = useAuth();
  const navigate = useNavigate();
  const [selectedCategory, setSelectedCategory] = useState(null);

  const {
    data: categories,
    isLoading: catLoading,
    isError: catError,
    error: catErrObj,
  } = useQuery({
    queryKey: ["categories"],
    queryFn: getCategories,
  });

  const {
    data: recipes,
    isLoading: recLoading,
    isError: recError,
    error: recErrObj,
  } = useQuery({
    queryKey: ["recipes", selectedCategory],
    queryFn: () => getRecipes(selectedCategory),
  });

  return (
    <div className="px-6 py-10 max-w-6xl mx-auto space-y-14">

      <div className="text-center">
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

        {isAuth && (
          <button
            onClick={() => navigate("/create")}
            className="mt-6 px-6 py-2 rounded-xl bg-accent text-white font-medium hover:opacity-90 transition"
          >
            + Create Recipe
          </button>
        )}
      </div>

      <div>
        <h2 className="text-2xl font-semibold mb-4">Categories</h2>

        {catError && (
          <ErrorMessage message={catErrObj?.message || "Failed to load categories"} />
        )}

        {catLoading ? (
          <p className="text-gray-500">Loading categories...</p>
        ) : (
          <div className="flex flex-wrap gap-3">
            {categories?.map((cat) => (
              <button
                key={cat.id}
                onClick={() => setSelectedCategory(cat.id)}
                className={`px-5 py-2 rounded-full text-sm font-medium border transition-all ${
                  selectedCategory === cat.id
                    ? "bg-accent text-white"
                    : "bg-white dark:bg-gray-900 hover:bg-accent hover:text-white"
                }`}
              >
                {cat.name}
              </button>
            ))}
          </div>
        )}
      </div>

      <div>
        <h2 className="text-2xl font-semibold mb-6">Latest Recipes</h2>

        {recError && (
          <ErrorMessage message={recErrObj?.message || "Failed to load recipes"} />
        )}

        {recLoading ? (
          <p className="text-gray-500">Loading recipes...</p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
            {recipes?.map((recipe) => (
              <RecipeCard
                key={recipe.id}
                recipe={recipe}
                navigate={navigate}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

function RecipeCard({ recipe, navigate }) {
  return (
    <div
      onClick={() => navigate(`/post/${recipe.id}`)}
      className="bg-white dark:bg-gray-900 rounded-2xl overflow-hidden shadow-soft hover:shadow-xl transition-all cursor-pointer"
    >
      <img
        src={recipe.imageUrl}
        alt={recipe.title}
        className="h-44 w-full object-cover"
      />

      <div className="p-4">
        <h3 className="font-semibold text-lg">{recipe.title}</h3>
        <p className="text-sm text-gray-500 mt-1">
          {recipe.category?.name}
        </p>
      </div>
    </div>
  );
}