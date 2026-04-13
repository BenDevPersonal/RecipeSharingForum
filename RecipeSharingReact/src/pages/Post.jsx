import { useParams, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { useAuth } from "../context/AuthContext";

const mockPosts = [
  {
    id: 1,
    author: "chefanna",
    title: "Classic Margherita Pizza",
    content: "A simple Italian pizza with tomatoes, mozzarella, and basil.",
    creationDate: "2026-01-10",
    updateDate: "2026-01-10",
    allergies: [],
    categories: ["italian"],
    feedbacks: [
      { author: "john", rating: 5, content: "Amazing flavor and texture!" },
      { author: "lisa", rating: 4, content: "Really good, will make again." }
    ]
  },
  {
    id: 2,
    author: "sweetbaker",
    title: "Chocolate Cake",
    content: "Rich and moist chocolate cake with ganache frosting.",
    creationDate: "2026-02-01",
    updateDate: "2026-02-01",
    allergies: ["eggs"],
    categories: ["dessert"],
    feedbacks: [
      { author: "mike", rating: 5, content: "Best cake ever!" }
    ]
  }
];

function getPostById(id) {
  const post = mockPosts.find((p) => p.id === Number(id));
  return post ?? null;
}

export function Post() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuth } = useAuth();

  const [rating, setRating] = useState(0);
  const [text, setText] = useState("");

  const { data: post, isLoading } = useQuery({
    queryKey: ["post", id],
    queryFn: () => getPostById(id),
  });

  if (isLoading) {
    return (
      <div className="max-w-4xl mx-auto px-6 py-10 text-gray-500">
        Loading post...
      </div>
    );
  }

  if (!post) {
    return (
      <div className="max-w-4xl mx-auto px-6 py-10 text-center space-y-4">
        <p className="text-gray-500">Post not found</p>

        <button
          onClick={() => navigate("/")}
          className="px-4 py-2 rounded-lg bg-accent text-white"
        >
          Go Home
        </button>
      </div>
    );
  }

  const avgRating =
    post.feedbacks.length > 0
      ? post.feedbacks.reduce((a, b) => a + b.rating, 0) /
        post.feedbacks.length
      : 0;

  return (
    <div className="max-w-4xl mx-auto px-6 py-10 space-y-8">

      <div className="bg-white dark:bg-gray-900 rounded-2xl shadow-soft p-6 space-y-3">
        <h1 className="text-3xl font-bold">{post.title}</h1>

        <p className="text-gray-500">by {post.author}</p>

        <div className="flex flex-wrap gap-2 text-xs">
          {post.categories.map((c) => (
            <span key={c} className="px-2 py-1 rounded-full bg-accent text-white">
              {c}
            </span>
          ))}

          {post.allergies.map((a) => (
            <span key={a} className="px-2 py-1 rounded-full bg-red-500 text-white">
              no {a}
            </span>
          ))}
        </div>

        <div className="text-sm text-gray-500">
          Created: {post.creationDate} · Updated: {post.updateDate}
        </div>

        <div className="text-lg font-semibold">
          Rating: {avgRating ? avgRating.toFixed(1) : "No ratings"} ⭐
        </div>
      </div>

      <div className="bg-white dark:bg-gray-900 rounded-2xl shadow-soft p-6">
        <p className="text-gray-700 dark:text-gray-300 leading-relaxed">
          {post.content}
        </p>
      </div>

      <div className="space-y-4">
        <h2 className="text-xl font-semibold">Feedback</h2>

        {post.feedbacks.map((f, i) => (
          <div key={i} className="bg-white dark:bg-gray-900 rounded-2xl shadow-soft p-4">
            <div className="flex justify-between">
              <span className="font-medium">{f.author}</span>
              <span className="text-accent font-semibold">
                {f.rating} ⭐
              </span>
            </div>

            <p className="text-gray-600 dark:text-gray-400 mt-2">
              {f.content}
            </p>
          </div>
        ))}
      </div>

      {isAuth ? (
        <div className="bg-white dark:bg-gray-900 rounded-2xl shadow-soft p-6 space-y-4">
          <h3 className="font-semibold">Leave feedback</h3>

          <div className="flex gap-2">
            {[1,2,3,4,5].map((s) => (
              <button
                key={s}
                onClick={() => setRating(s)}
                className={`text-2xl ${s <= rating ? "text-yellow-400" : "text-gray-300"}`}
              >
                ★
              </button>
            ))}
          </div>

          <textarea
            value={text}
            onChange={(e) => setText(e.target.value)}
            className="w-full p-3 rounded-xl bg-gray-100 dark:bg-gray-800"
            placeholder="Write your feedback..."
          />

          <button className="px-4 py-2 bg-accent text-white rounded-xl">
            Submit
          </button>
        </div>
      ) : (
        <div className="text-center text-gray-500">
          Login to leave feedback
        </div>
      )}

    </div>
  );
}