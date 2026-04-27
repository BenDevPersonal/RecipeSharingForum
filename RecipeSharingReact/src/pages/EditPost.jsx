import { useParams, useNavigate } from "react-router-dom";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useState, useEffect } from "react";

import { getPostById, updatePost } from "../api/posts";
import { getCategories } from "../api/categories";
import { getAllergies } from "../api/meta";
import { ErrorMessage } from "../components/ErrorMessage";

export function EditPost() {
  const { id } = useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const [selectedCategories, setSelectedCategories] = useState([]);
  const [selectedAllergies, setSelectedAllergies] = useState([]);

  const { data: post, isLoading, isError, error } = useQuery({
    queryKey: ["post", id],
    queryFn: () => getPostById(id),
    enabled: !!id,
  });

  const { data: categories } = useQuery({
    queryKey: ["categories"],
    queryFn: getCategories,
  });

  const { data: allergies } = useQuery({
    queryKey: ["allergies"],
    queryFn: getAllergies,
  });

  useEffect(() => {
    if (!post) return;

    setTitle(post.title || "");
    setContent(post.content || "");
  }, [post]);

  // 🔥 IMPORTANT: map STRING names → IDs once data is loaded
  useEffect(() => {
    if (!post || !categories || !allergies) return;

    const categoryIds = categories
      .filter((c) => post.categories?.includes(c.name))
      .map((c) => c.id);

    const allergyIds = allergies
      .filter((a) => post.allergies?.includes(a.name))
      .map((a) => a.id);

    setSelectedCategories(categoryIds);
    setSelectedAllergies(allergyIds);
  }, [post, categories, allergies]);

  const updateMutation = useMutation({
    mutationFn: ({ id, data }) => updatePost(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["post", id] });
      queryClient.invalidateQueries({ queryKey: ["posts"] });
      navigate(`/post/${id}`);
    },
  });

  function toggle(setter, list, value) {
    setter(
      list.includes(value)
        ? list.filter((v) => v !== value)
        : [...list, value]
    );
  }

  function handleSubmit() {
    if (!title.trim() || !content.trim()) return;

    updateMutation.mutate({
      id,
      data: {
        title,
        content,
        categoryIds: selectedCategories,
        allergyIds: selectedAllergies,
      },
    });
  }

  if (isLoading) return <div className="p-6">Loading...</div>;
  if (isError) return <ErrorMessage message={error.message} />;
  if (!post) return <div className="p-6">Recipe not found</div>;

  return (
    <div className="max-w-3xl mx-auto px-6 py-10 space-y-6">

      <h1 className="text-2xl font-bold">Edit Recipe</h1>

      <input
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        className="w-full p-3 rounded-xl bg-gray-100 dark:bg-gray-800"
        placeholder="Title"
      />

      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        className="w-full p-3 rounded-xl bg-gray-100 dark:bg-gray-800"
        rows={8}
        placeholder="Content"
      />

      {/* CATEGORIES */}
      <div>
        <h2 className="font-semibold mb-2">Categories</h2>

        <div className="flex flex-wrap gap-2">
          {categories?.map((c) => (
            <button
              key={c.id}
              type="button"
              onClick={() =>
                toggle(setSelectedCategories, selectedCategories, c.id)
              }
              className={`px-3 py-1 rounded-full text-sm border ${
                selectedCategories.includes(c.id)
                  ? "bg-accent text-white"
                  : "bg-white dark:bg-gray-900"
              }`}
            >
              {c.name}
            </button>
          ))}
        </div>
      </div>

      {/* ALLERGIES */}
      <div>
        <h2 className="font-semibold mb-2">Allergies</h2>

        <div className="flex flex-wrap gap-2">
          {allergies?.map((a) => (
            <button
              key={a.id}
              type="button"
              onClick={() =>
                toggle(setSelectedAllergies, selectedAllergies, a.id)
              }
              className={`px-3 py-1 rounded-full text-sm border ${
                selectedAllergies.includes(a.id)
                  ? "bg-red-500 text-white"
                  : "bg-white dark:bg-gray-900"
              }`}
            >
              {a.name}
            </button>
          ))}
        </div>
      </div>

      {/* ACTIONS */}
      <div className="flex gap-3 pt-2">
        <button
          onClick={handleSubmit}
          className="px-4 py-2 bg-blue-600 text-white rounded-xl"
        >
          Save
        </button>

        <button
          onClick={() => navigate(-1)}
          className="px-4 py-2 bg-gray-200 dark:bg-gray-700 rounded-xl"
        >
          Cancel
        </button>
      </div>
    </div>
  );
}