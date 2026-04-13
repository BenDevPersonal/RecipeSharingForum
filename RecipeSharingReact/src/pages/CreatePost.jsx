import { useState } from "react";

const mockCategories = [
  { id: 1, name: "Breakfast" },
  { id: 2, name: "Lunch" },
  { id: 3, name: "Dinner" },
  { id: 4, name: "Dessert" },
  { id: 5, name: "Vegan" },
];

const mockAllergies = [
  { id: 1, name: "Gluten" },
  { id: 2, name: "Lactose" },
  { id: 3, name: "Nuts" },
  { id: 4, name: "Eggs" },
];

export function CreatePost() {
  const [post, setPost] = useState({
    title: "",
    content: "",
    categories: [],
    allergies: [],
  });

  const [errors, setErrors] = useState({
    title: false,
    content: false,
  });

  function toggleItem(field, item) {
    setPost((prev) => {
      const exists = prev[field].includes(item);

      return {
        ...prev,
        [field]: exists
          ? prev[field].filter((x) => x !== item)
          : [...prev[field], item],
      };
    });
  }

  function validate() {
    const newErrors = {
      title: post.title.trim() === "",
      content: post.content.trim() === "",
    };

    setErrors(newErrors);

    return !newErrors.title && !newErrors.content;
  }

  function handleSubmit() {
    if (!validate()) return;

    console.log(post);

    setPost({
      title: "",
      content: "",
      categories: [],
      allergies: [],
    });

    setErrors({ title: false, content: false });
  }

  return (
    <div className="max-w-4xl mx-auto px-6 py-10 space-y-10">

      <h1 className="text-3xl font-bold">Create Post</h1>

      <div className="space-y-4">

        <input
          value={post.title}
          onChange={(e) => setPost({ ...post, title: e.target.value })}
          placeholder="Title"
          className={`w-full p-4 rounded-2xl bg-gray-50 dark:bg-gray-800 border outline-none focus:ring-2 focus:ring-accent ${
            errors.title
              ? "border-red-500"
              : "border-gray-200 dark:border-gray-700"
          }`}
        />

        {errors.title && (
          <p className="text-red-500 text-sm">Title is required</p>
        )}

        <textarea
          value={post.content}
          onChange={(e) => setPost({ ...post, content: e.target.value })}
          placeholder="Write your recipe..."
          className={`w-full p-4 h-40 rounded-2xl bg-gray-50 dark:bg-gray-800 border outline-none focus:ring-2 focus:ring-accent resize-none ${
            errors.content
              ? "border-red-500"
              : "border-gray-200 dark:border-gray-700"
          }`}
        />

        {errors.content && (
          <p className="text-red-500 text-sm">Content is required</p>
        )}

      </div>

      <div>
        <h2 className="text-lg font-semibold mb-3">Categories</h2>

        <div className="flex flex-wrap gap-2">
          {mockCategories.map((cat) => (
            <button
              key={cat.id}
              onClick={() => toggleItem("categories", cat.name)}
              className={`px-4 py-2 rounded-full border transition text-sm ${
                post.categories.includes(cat.name)
                  ? "bg-accent text-white border-accent"
                  : "bg-white dark:bg-gray-900 border-gray-300 dark:border-gray-700 hover:bg-accent hover:text-white"
              }`}
            >
              {cat.name}
            </button>
          ))}
        </div>
      </div>

      <div>
        <h2 className="text-lg font-semibold mb-3">Allergies</h2>

        <div className="flex flex-wrap gap-2">
          {mockAllergies.map((allergy) => (
            <button
              key={allergy.id}
              onClick={() => toggleItem("allergies", allergy.name)}
              className={`px-4 py-2 rounded-full border transition text-sm ${
                post.allergies.includes(allergy.name)
                  ? "bg-red-500 text-white border-red-500"
                  : "bg-white dark:bg-gray-900 border-gray-300 dark:border-gray-700 hover:bg-red-500 hover:text-white"
              }`}
            >
              {allergy.name}
            </button>
          ))}
        </div>
      </div>

      <button
        onClick={handleSubmit}
        className="px-6 py-3 bg-accent text-white rounded-full hover:bg-accentDark transition shadow-soft"
      >
        Publish Post
      </button>

    </div>
  );
}