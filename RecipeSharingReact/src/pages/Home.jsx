import { useState, useMemo } from "react";
import { useQuery } from "@tanstack/react-query";
import { getCategories } from "../api/categories";
import { getPosts } from "../api/posts";
import { useNavigate } from "react-router-dom";
import { ErrorMessage } from "../components/ErrorMessage";
import { useAuth } from "../context/useAuth";
import { parseJwt } from "../utils/jwt";

import { getMe } from "../api/users";
import { getUserSetting } from "../api/settings";

export function Home() {
const BOOKMARKS = -1;
  const { isAuth, token } = useAuth();
  const navigate = useNavigate();

  const [selectedCategory, setSelectedCategory] = useState(null);

  const user = token ? parseJwt(token)?.sub : null;

  // -----------------------------
  // USER
  // -----------------------------
  const { data: me } = useQuery({
    queryKey: ["me"],
    queryFn: getMe,
    enabled: isAuth,
  });

  // -----------------------------
  // SETTINGS
  // -----------------------------
  const { data: settings } = useQuery({
    queryKey: ["settings", me?.id],
    queryFn: () => getUserSetting(me.id),
    enabled: !!me?.id,
    retry: false,
  });

  // -----------------------------
  // DATA
  // -----------------------------
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
    queryFn: getPosts,
  });

  // -----------------------------
  // AUTO ALLERGY EXCLUSION
  // -----------------------------
  const excludedAllergies = useMemo(() => {
    if (!settings?.autoFilterAllergy) return [];

    return me?.allergies || [];
  }, [settings, me]);

  // -----------------------------
  // FILTER POSTS
  // -----------------------------
  const filteredPosts = useMemo(() => {
    if (!posts) return [];

    // BOOKMARK MODE
    if (selectedCategory === BOOKMARKS) {
      return posts.filter((p) => p.bookmarked === true); // OR use API (better below)
    }

    return posts.filter((post) => {
      const categoryMatch =
        !selectedCategory ||
        post.categories?.includes(
          categories?.find((c) => c.id === selectedCategory)?.name
        );

      const allergyMatch =
        excludedAllergies.length === 0 ||
        !post.allergies?.some((a) =>
          excludedAllergies.includes(a)
        );

      return categoryMatch && allergyMatch;
    });
  }, [posts, selectedCategory, categories, excludedAllergies]);

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

        {catError && <ErrorMessage message={catErrObj?.message} />}

        {catLoading ? (
          <p>Loading categories...</p>
        ) : (
          <div className="flex flex-wrap gap-3">

          <button
            onClick={() => setSelectedCategory(BOOKMARKS)}
            className={`px-5 py-2 rounded-full border ${
              selectedCategory === BOOKMARKS ? "bg-accent text-white" : ""
            }`}
          >
            Bookmarks
          </button>

            <button
              onClick={() => setSelectedCategory(null)}
              className={`px-5 py-2 rounded-full border ${
                selectedCategory === null ? "bg-accent text-white" : ""
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

        {recError && <ErrorMessage message={recErrObj?.message} />}

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

      {recipe.images?.length > 0 && (
        <img
          src={`http://localhost:8080/images/${recipe.images[0]}`}
          alt={recipe.title}
          className="h-44 w-full object-cover"
        />
      )}

      <div className="p-4">
        <h3 className="font-semibold">{recipe.title}</h3>
      </div>
    </div>
  );
}