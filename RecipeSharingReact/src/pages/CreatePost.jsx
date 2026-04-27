import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useAuth } from "../context/useAuth";

import { createPost } from "../api/posts";
import { getCategories } from "../api/categories";
import { getAllergies } from "../api/allergies";

import { parseJwt } from "../utils/jwt";

export function CreatePost() {
  const { token, isAuth } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const user = token ? parseJwt(token)?.sub : null;

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

  const [backendError, setBackendError] = useState("");

  const { data: categories = [] } = useQuery({
    queryKey: ["categories"],
    queryFn: getCategories,
  });

  const { data: allergies = [] } = useQuery({
    queryKey: ["allergies"],
    queryFn: getAllergies,
  });

  const mutation = useMutation({
    mutationFn: createPost,

    onSuccess: (data) => {
      queryClient.invalidateQueries(["posts"]);
      setBackendError("");

      setPost({
        title: "",
        content: "",
        categories: [],
        allergies: [],
      });

      navigate(`/post/${data.id}`);
    },

    onError: (error) => {
      const message =
        error?.response?.data?.message ||
        error?.response?.data?.error ||
        error.message ||
        "Something went wrong";

      setBackendError(message);
    },
  });

  function toggleItem(field, name) {
    setPost((prev) => {
      const exists = prev[field].includes(name);

      return {
        ...prev,
        [field]: exists
          ? prev[field].filter((x) => x !== name)
          : [...prev[field], name],
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

    if (!isAuth) {
      setBackendError("You must be logged in to create a recipe.");
      return;
    }

    mutation.mutate({
      title: post.title,
      content: post.content,
      userId: user.id,

      categoryIds: categories
        .filter((c) => post.categories.includes(c.name))
        .map((c) => c.id),

      allergyIds: allergies
        .filter((a) => post.allergies.includes(a.name))
        .map((a) => a.id),

      creationDate: new Date().toISOString().split("T")[0],
      updateDate: new Date().toISOString().split("T")[0],
    });
  }

  return (
    <div className="max-w-4xl mx-auto px-6 py-10 space-y-10">

      <h1 className="text-3xl font-bold">Create Recipe</h1>

      {backendError && (
        <div className="p-4 rounded-xl bg-red-100 text-red-700 border border-red-300">
          {backendError}
        </div>
      )}

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

      {/* CATEGORIES */}
      <div>
        <h2 className="text-lg font-semibold mb-3">Categories</h2>

        <div className="flex flex-wrap gap-2">
          {categories.map((cat) => (
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

      {/* ALLERGIES */}
      <div>
        <h2 className="text-lg font-semibold mb-3">Allergies</h2>

        <div className="flex flex-wrap gap-2">
          {allergies.map((allergy) => (
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
        disabled={mutation.isPending}
        className="px-6 py-3 bg-accent text-white rounded-full hover:bg-accentDark transition shadow-soft disabled:opacity-50"
      >
        {mutation.isPending ? "Publishing..." : "Publish Recipe"}
      </button>
    </div>
  );
}