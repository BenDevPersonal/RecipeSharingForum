import { useContext, useState } from "react";
import { AppContext } from "../App";
import { useQuery } from "@tanstack/react-query";
import { getCategories } from "../api/categories";
import { getRecipes } from "../api/recipes";

export function Home() {
  const { user } = useContext(AppContext);
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
    <div className="px-6 py-10 max-w-6xl mx-auto">
      <Hero user={user} />

      <CategoriesSection
        categories={categories}
        loading={catLoading}
        error={catError}
        errorObj={catErrObj}
        selectedCategory={selectedCategory}
        setSelectedCategory={setSelectedCategory}
      />

      <RecipesSection
        recipes={recipes}
        loading={recLoading}
        error={recError}
        errorObj={recErrObj}
      />
    </div>
  );
}

{/* Components */}

function Hero({ user }) {
  return (
    <div className="text-center mb-14">
      <h1 className="text-5xl font-bold mb-4 tracking-tight">
        🍳 Discover & Share Recipes
      </h1>

      <p className="text-gray-500 dark:text-gray-400 text-lg">
        Join a community of food lovers and explore amazing dishes
      </p>

      <p className="mt-3 text-accent font-medium">
        {user !== "None"
          ? `Welcome back, ${user}!`
          : "Start sharing your recipes today"}
      </p>
    </div>
  );
}

function CategoriesSection({
  categories,
  loading,
  error,
  errorObj,
  selectedCategory,
  setSelectedCategory,
}) {
  return (
    <div className="mb-12">
      <h2 className="text-2xl font-semibold mb-4">Categories</h2>

      {error && <ErrorMessage message={errorObj.message} />}

      {loading ? (
        <p className="text-gray-500">Loading categories...</p>
      ) : (
        <div className="flex flex-wrap gap-3">
          {categories?.map((cat) => (
            <CategoryButton
              key={cat.id}
              category={cat}
              isSelected={selectedCategory === cat.id}
              onClick={() => setSelectedCategory(cat.id)}
            />
          ))}
        </div>
      )}
    </div>
  );
}

function CategoryButton({ category, isSelected, onClick }) {
  return (
    <button
      onClick={onClick}
      className={`px-5 py-2 rounded-full text-sm font-medium border transition-all
        ${
          isSelected
            ? "bg-accent text-white shadow-soft"
            : "bg-white dark:bg-gray-900 border-gray-200 dark:border-gray-700 hover:bg-accent hover:text-white hover:shadow-soft"
        }`}
    >
      {category.name}
    </button>
  );
}

function RecipesSection({ recipes, loading, error, errorObj }) {
  return (
    <div>
      <h2 className="text-2xl font-semibold mb-6">Latest Recipes</h2>

      {error && <ErrorMessage message={errorObj.message} />}

      {loading ? (
        <p className="text-gray-500">Loading recipes...</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
          {recipes?.map((recipe) => (
            <RecipeCard key={recipe.id} recipe={recipe} />
          ))}
        </div>
      )}
    </div>
  );
}

function RecipeCard({ recipe }) {
  return (
    <div className="bg-white dark:bg-gray-900 rounded-2xl overflow-hidden shadow-soft hover:shadow-xl transition-all duration-300 cursor-pointer group">
      <div className="overflow-hidden">
        <img
          src={recipe.imageUrl}
          alt={recipe.title}
          className="h-44 w-full object-cover group-hover:scale-105 transition duration-300"
        />
      </div>

      <div className="p-4">
        <h3 className="font-semibold text-lg group-hover:text-accent transition">
          {recipe.title}
        </h3>

        <p className="text-sm text-gray-500 dark:text-gray-400 mt-1">
          {recipe.category?.name}
        </p>
      </div>
    </div>
  );
}

function ErrorMessage({ message }) {
  return (
    <div className="mb-6 p-4 rounded-lg bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400">
      {message}
    </div>
  );
}