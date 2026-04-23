import { toggleBookmark } from "../api/bookmarks";
import { useParams, useNavigate } from "react-router-dom";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useState, useMemo } from "react";
import { useEffect } from "react";
import { useAuth } from "../context/useAuth";
import { getPostById, deletePost } from "../api/posts";
import {
  updateFeedback,
  deleteFeedback,
  createFeedback,
} from "../api/feedbacks";
import { getMe } from "../api/users";

import { parseJwt } from "../utils/jwt";
import { ErrorMessage } from "../components/ErrorMessage";
import { DropdownMenu } from "../components/DropdownMenu";

/* Icons */
const EditIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
    <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25z" />
  </svg>
);

const DeleteIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
    <path d="M6 7h12l-1 14H7L6 7zm3-3h6l1 2H8l1-2z" />
  </svg>
);

export function Post() {
  const { id } = useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { isAuth, token } = useAuth();

  const [editingFeedbackId, setEditingFeedbackId] = useState(null);
  const [editContent, setEditContent] = useState("");
  const [editRating, setEditRating] = useState(1);

  const [newRating, setNewRating] = useState(1);
  const [newContent, setNewContent] = useState("");
  const [isBookmarked, setIsBookmarked] = useState(false);

  const currentUser = useMemo(() => {
    if (!token) return null;
    try {
      const p = parseJwt(token);
      return p?.username || p?.sub || null;
    } catch {
      return null;
    }
  }, [token]);

  const { data: post, isLoading, isError, error } = useQuery({
    queryKey: ["post", id],
    queryFn: () => getPostById(id),
    enabled: !!id,
  });

  useEffect(() => {
    if (post) {
      setIsBookmarked(post.bookmarked);
    }
  }, [post]);

  const { data: me } = useQuery({
    queryKey: ["me"],
    queryFn: getMe,
    enabled: isAuth,
  });

  const deletePostMutation = useMutation({
    mutationFn: deletePost,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["posts"] });
      navigate("/");
    },
  });

  const createFeedbackMutation = useMutation({
    mutationFn: createFeedback,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["post", id] });
      setNewRating(1);
      setNewContent("");
    },
  });

  const updateFeedbackMutation = useMutation({
    mutationFn: updateFeedback,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["post", id] });
      setEditingFeedbackId(null);
    },
  });

  const deleteFeedbackMutation = useMutation({
    mutationFn: deleteFeedback,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["post", id] });
    },
  });

const bookmarkMutation = useMutation({
  mutationFn: toggleBookmark,
  onSuccess: () => {
    queryClient.invalidateQueries({ queryKey: ["post", id] });
  },
});

  if (isLoading) return <div className="p-6">Loading...</div>;
  if (isError) return <ErrorMessage message={error.message} />;
  if (!post) return <div className="p-6">Post not found</div>;

  const feedbacks = [...(post.feedbacks || [])].sort((a, b) => b.id - a.id);

  const isPostOwner = isAuth && post.author === currentUser;
  const userFeedback = feedbacks.find((f) => f.author === currentUser);
  const hasUserFeedback = !!userFeedback;

  const avgRating =
    feedbacks.length > 0
      ? (
          feedbacks.reduce((sum, f) => sum + (f.rating || 0), 0) /
          feedbacks.length
        ).toFixed(1)
      : null;

  const isEdited =
    post.updateDate && post.creationDate
      ? post.updateDate !== post.creationDate
      : false;

  const hasAllergyConflict =
    me?.allergies &&
    post.allergies &&
    post.allergies.some((a) => me.allergies.includes(a));

  const StarSelector = ({ value, setValue }) => (
    <div className="flex gap-1">
      {[1, 2, 3, 4, 5].map((n) => (
        <button
          key={n}
          type="button"
          onClick={() => setValue(n)}
          className={`text-lg transition hover:scale-110 ${
            n <= value
              ? "text-yellow-400 drop-shadow"
              : "text-gray-300 dark:text-gray-600"
          }`}
        >
          ★
        </button>
      ))}
    </div>
  );

  return (
    <div className="max-w-4xl mx-auto px-6 py-10 space-y-8">

      {/* POST */}
      <div className="relative bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 p-6 rounded-2xl space-y-4">

        {hasAllergyConflict && (
          <div className="text-sm px-3 py-2 rounded-lg bg-red-100 dark:bg-red-900 text-red-700 dark:text-red-300">
            Warning: This post contains ingredients matching your allergies
          </div>
        )}

        <div className="flex items-start justify-between gap-4">
          <div className="flex flex-col gap-2">
            <h1 className="text-3xl font-bold">{post.title}</h1>

            <div
              onClick={() => navigate(`/user/${post.authorId}`)}
              className="text-sm text-gray-500 cursor-pointer hover:underline w-fit"
            >
              by {post.author}
            </div>

            <div className="flex items-center gap-2">
              {avgRating ? (
                <span className="text-sm px-3 py-1 rounded-full bg-yellow-100 dark:bg-yellow-900 text-yellow-700 dark:text-yellow-300">
                  {avgRating} ★
                </span>
              ) : (
                <span className="text-sm text-gray-400">No ratings</span>
              )}
            </div>

            <div className="flex flex-wrap gap-2 mt-1">
              {post.categories?.map((c) => (
                <span
                  key={c}
                  className="text-xs px-2 py-1 rounded-full bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300"
                >
                  {c}
                </span>
              ))}

              {post.allergies?.map((a) => (
                <span
                  key={a}
                  className="text-xs px-2 py-1 rounded-full bg-red-100 dark:bg-red-900 text-red-700 dark:text-red-300"
                >
                  {a}
                </span>
              ))}
            </div>
          </div>

          {isPostOwner && (
            <DropdownMenu>
              {(close) => (
                <div className="flex flex-col">

                  <button
                    onClick={() => {
                      close();
                      navigate(`/posts/edit/${post.id}`);
                    }}
                    className="flex items-center gap-2 w-full px-3 py-2 text-sm hover:bg-gray-100 dark:hover:bg-gray-800 text-left"
                  >
                    <EditIcon />
                    Edit
                  </button>

                  <button
                    onClick={() => {
                      close();
                      deletePostMutation.mutate(post.id);
                    }}
                    className="flex items-center gap-2 w-full px-3 py-2 text-sm hover:bg-gray-100 dark:hover:bg-gray-800 text-left text-red-600"
                  >
                    <DeleteIcon />
                    Delete
                  </button>

                </div>
              )}
            </DropdownMenu>
          )}
        </div>

        <p className="whitespace-pre-line text-gray-700 dark:text-gray-300">
          {post.content}
        </p>

        <div className="grid gap-4 mt-4">
          {post.images?.map((img, i) => (
            <img
              key={i}
              src={`http://localhost:8080/images/${img}`}
              className="rounded-xl w-full object-cover"
              alt={`post-image-${i}`}
            />
          ))}
        </div>

        <div className="text-xs text-gray-500 flex gap-2">
          <span>Created: {post.creationDate}</span>
          {isEdited && <span>(edited)</span>}
        </div>
      </div>

     <button
       onClick={() => bookmarkMutation.mutate(post.id)}
       className={`px-4 py-2 rounded-lg transition ${
         isBookmarked
           ? "bg-yellow-400 text-black"
           : "bg-gray-200 dark:bg-gray-800"
       }`}
     >
       {isBookmarked ? "🔖 Bookmarked" : "🔖 Bookmark"}
     </button>

      {/* FEEDBACK */}
      <div className="space-y-4">
        <h2 className="text-xl font-semibold">Feedback</h2>

        {feedbacks.map((f) => {
          const isOwner = isAuth && f.author === currentUser;

          return (
            <div
              key={f.id}
              className="p-4 rounded-xl bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800"
            >
              <div className="flex justify-between items-start">
                <div className="flex items-center gap-3 mb-2">
                  <span
                    onClick={() => navigate(`/user/${f.authorId}`)}
                    className="text-sm font-medium cursor-pointer hover:underline"
                  >
                    {f.author}
                  </span>
                  <span className="text-xs px-2 py-1 rounded-full bg-yellow-100 dark:bg-yellow-900 text-yellow-700 dark:text-yellow-300">
                    {f.rating} ★
                  </span>
                </div>

                {isOwner && (
                  <DropdownMenu>
                    {(close) => (
                      <div className="flex flex-col">

                        <button
                          onClick={() => {
                            close();
                            setEditingFeedbackId(f.id);
                            setEditContent(f.content);
                            setEditRating(f.rating);
                          }}
                          className="flex items-center gap-2 w-full px-3 py-2 text-sm hover:bg-gray-100 dark:hover:bg-gray-800 text-left"
                        >
                          <EditIcon />
                          Edit
                        </button>

                        <button
                          onClick={() => {
                            close();
                            deleteFeedbackMutation.mutate(f.id);
                          }}
                          className="flex items-center gap-2 w-full px-3 py-2 text-sm hover:bg-gray-100 dark:hover:bg-gray-800 text-left text-red-600"
                        >
                          <DeleteIcon />
                          Delete
                        </button>

                      </div>
                    )}
                  </DropdownMenu>
                )}
              </div>

              {editingFeedbackId === f.id ? (
                <div className="mt-3 p-4 rounded-xl border border-blue-200 dark:border-blue-800 bg-blue-50/40 dark:bg-blue-900/20 space-y-3">

                  <div className="flex justify-between items-center">
                    <span className="text-xs text-gray-500">Edit feedback</span>
                    <button
                      onClick={() => setEditingFeedbackId(null)}
                      className="text-xs text-gray-500 hover:text-red-500"
                    >
                      ✕
                    </button>
                  </div>

                  <StarSelector value={editRating} setValue={setEditRating} />

                  <textarea
                    value={editContent}
                    onChange={(e) => setEditContent(e.target.value)}
                    rows={3}
                    className="w-full p-3 text-sm rounded-lg border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />

                  <div className="flex justify-end gap-2">
                    <button
                      onClick={() => setEditingFeedbackId(null)}
                      className="px-3 py-1.5 text-sm rounded-lg bg-gray-200 dark:bg-gray-700"
                    >
                      Cancel
                    </button>

                    <button
                      onClick={() =>
                        updateFeedbackMutation.mutate({
                          id: f.id,
                          data: {
                            postId: post.id,
                            rating: editRating,
                            content: editContent,
                          },
                        })
                      }
                      className="px-4 py-1.5 text-sm rounded-lg bg-blue-600 text-white"
                    >
                      Save
                    </button>
                  </div>
                </div>
              ) : (
                <p className="text-gray-700 dark:text-gray-300">
                  {f.content}
                </p>
              )}
            </div>
          );
        })}

        {isAuth && !isPostOwner && !hasUserFeedback && (
          <div className="p-4 rounded-xl bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-800 space-y-3">
            <h3 className="font-semibold">Leave feedback</h3>

            <StarSelector value={newRating} setValue={setNewRating} />

            <textarea
              value={newContent}
              onChange={(e) => setNewContent(e.target.value)}
              className="w-full p-3 rounded bg-gray-100 dark:bg-gray-800"
            />

            <button
              onClick={() =>
                createFeedbackMutation.mutate({
                  postId: post.id,
                  rating: newRating,
                  content: newContent,
                })
              }
              className="px-4 py-2 bg-blue-600 text-white rounded"
            >
              Submit
            </button>
          </div>
        )}
        {(!isAuth || hasUserFeedback) && (
          <div className="text-sm text-gray-400 text-center">
            {!isAuth
              ? "You need to be logged in to leave feedback"
              : "You have already left feedback for this post"}
          </div>
        )}
      </div>
    </div>
  );
}