import { useState, useMemo } from "react";
import { useQuery } from "@tanstack/react-query";
import { getCategories } from "../api/categories";
import { getPosts } from "../api/posts"; // ✅ changed
import { useNavigate } from "react-router-dom";
import { ErrorMessage } from "../components/ErrorMessage";
import { useAuth } from "../context/useAuth";
import { parseJwt } from "../utils/jwt";

export function Home() {
  const { isAuth, token } = useAuth();
  const navigate = useNavigate();

  const [selectedCategory, setSelectedCategory] = useState(null);

  const user = token ? parseJwt(token)?.sub : null;

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
    data: posts,
    isLoading: recLoading,
    isError: recError,
    error: recErrObj,
  } = useQuery({
    queryKey: ["posts"],
    queryFn: getPosts, // ✅ no param anymore
  });

  // 🔥 FILTER LOGIC HERE
  const filteredPosts = useMemo(() => {
    if (!posts) return [];

    if (!selectedCategory) return posts;

    const selected = categories?.find((c) => c.id === selectedCategory);

    if (!selected) return posts;

    return posts.filter((post) =>
      post.categories?.includes(selected.name)
    );
  }, [posts, selectedCategory, categories]);

  function handleCategoryClick(id) {
    setSelectedCategory((prev) => (prev === id ? null : id));
  }

  return (
    <div className="px-6 py-10 max-w-6xl mx-auto space-y-14">

      {/* HERO */}
      <div className="text-center">
        <h1 className="text-5xl font-bold mb-4">
          🍳 Discover & Share Recipes
        </h1>

        <p className="text-gray-500 text-lg">
          Join a community of food lovers
        </p>

        <p className="mt-3 text-accent font-medium">
          {user
            ? `Welcome back, ${user}!`
            : "Start sharing your recipes today"}
        </p>

        {isAuth && (
          <button
            onClick={() => navigate("/create")}
            className="mt-6 px-6 py-2 rounded-xl bg-accent text-white"
          >
            + Create Recipe
          </button>
        )}
      </div>

      {/* CATEGORIES */}
      <div>
        <h2 className="text-2xl font-semibold mb-4">Categories</h2>

        {catError && (
          <ErrorMessage message={catErrObj?.message} />
        )}

        {catLoading ? (
          <p>Loading categories...</p>
        ) : (
          <div className="flex flex-wrap gap-3">
            <button
              onClick={() => setSelectedCategory(null)}
              className={`px-5 py-2 rounded-full border ${
                selectedCategory === null
                  ? "bg-accent text-white"
                  : ""
              }`}
            >
              All
            </button>

            {categories?.map((cat) => (
              <button
                key={cat.id}
                onClick={() => handleCategoryClick(cat.id)}
                className={`px-5 py-2 rounded-full border ${
                  selectedCategory === cat.id
                    ? "bg-accent text-white"
                    : ""
                }`}
              >
                {cat.name}
              </button>
            ))}
          </div>
        )}
      </div>

      {/* RECIPES */}
      <div>
        <h2 className="text-2xl font-semibold mb-6">Latest Recipes</h2>

        {recError && (
          <ErrorMessage message={recErrObj?.message} />
        )}

        {recLoading ? (
          <p>Loading recipes...</p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
            {filteredPosts.map((recipe) => (
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
      className="bg-white dark:bg-gray-900 rounded-2xl overflow-hidden cursor-pointer"
    >
      <img
        src={recipe.imageUrl}
        alt={recipe.title}
        className="h-44 w-full object-cover"
      />

      <div className="p-4">
        <h3 className="font-semibold">{recipe.title}</h3>
      </div>
    </div>
  );
}