import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { SkeletonSection } from "../components/Skeleton";
import { DropdownMenu } from "../components/DropdownMenu";
import { highlightParts } from "../utils/highlight";
import { getMe, getUsers, updateProfile, deleteUser } from "../api/users";
import { getPosts, deletePost } from "../api/posts";
import { getFeedbacks, deleteFeedback } from "../api/feedbacks";
import {
  getAllergies,
  createAllergy,
  updateAllergy,
  deleteAllergy,
} from "../api/allergies";
import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../api/categories";

/* ================= UI HELPERS ================= */

function Card({ children }) {
  return (
    <div className="p-4 rounded-2xl bg-white dark:bg-gray-900 shadow-soft hover:shadow-lg transition-all duration-200">
      {children}
    </div>
  );
}

function FadeIn({ children }) {
  return (
    <div className="animate-[fadeIn_.25s_ease-in-out]">{children}</div>
  );
}

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

/* ================= DASHBOARD ================= */

export function AdminDashboard() {
  const [tab, setTab] = useState("Users");

  const { data: currentUser } = useQuery({
    queryKey: ["me"],
    queryFn: getMe,
  });

  const isModerator = currentUser?.role === "moderator";

  useEffect(() => {
    if (isModerator && (tab === "Categories" || tab === "Allergies")) {
      setTab("Users");
    }
  }, [isModerator, tab]);

  return (
    <div className="max-w-6xl mx-auto p-8">
      <h1 className="text-4xl font-bold mb-8 tracking-tight">
        🛠 Admin Dashboard
      </h1>

      <div className="flex gap-3 mb-8 flex-wrap">
        <Tab name="Users" tab={tab} setTab={setTab} />
        <Tab name="Recipes" tab={tab} setTab={setTab} />
        <Tab name="Feedbacks" tab={tab} setTab={setTab} />

        {!isModerator && (
          <>
            <Tab name="Categories" tab={tab} setTab={setTab} />
            <Tab name="Allergies" tab={tab} setTab={setTab} />
          </>
        )}
      </div>

      <FadeIn>
        {tab === "Users" && <UsersTab />}
        {tab === "Recipes" && <PostsTab />}
        {tab === "Feedbacks" && <FeedbacksTab />}

        {!isModerator && tab === "Categories" && <CategoriesTab />}
        {!isModerator && tab === "Allergies" && <AllergiesTab />}
      </FadeIn>
    </div>
  );
}

function Tab({ name, tab, setTab }) {
  const active = tab === name;

  return (
    <button
      onClick={() => setTab(name)}
      className={`px-4 py-2 rounded-xl text-sm font-medium transition-all duration-200
        ${active
          ? "bg-accent text-white shadow-md scale-105"
          : "bg-gray-200 dark:bg-gray-800 hover:scale-105"
        }`}
    >
      {name}
    </button>
  );
}

/* ================= USERS ================= */

function UsersTab() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const [search, setSearch] = useState("");

  const { data: currentUser } = useQuery({
    queryKey: ["me"],
    queryFn: getMe,
  });

  const { data: users = [], isLoading } = useQuery({
    queryKey: ["admin-users"],
    queryFn: getUsers,
  });

  const filteredUsers = users.filter((u) =>
    u.login.toLowerCase().includes(search.toLowerCase())
  );

  const deleteMutation = useMutation({
    mutationFn: deleteUser,
    onSuccess: () => queryClient.invalidateQueries(["admin-users"]),
  });

  const updateRoleMutation = useMutation({
    mutationFn: (updatedUser) =>
      updateProfile(updatedUser.id, updatedUser),
    onSuccess: () => queryClient.invalidateQueries(["admin-users"]),
  });

  const ROLE_LEVEL = { user: 1, moderator: 2, manager: 3, admin: 4 };

  function canModify(targetUser) {
    return (
      currentUser &&
      ROLE_LEVEL[currentUser.role] > ROLE_LEVEL[targetUser.role]
    );
  }

  function getAssignableRoles() {
    return Object.keys(ROLE_LEVEL).filter(
      (r) => ROLE_LEVEL[r] < ROLE_LEVEL[currentUser?.role]
    );
  }

  function buildUpdatePayload(user, role) {
    return {
      id: user.id,
      login: user.login,
      password: null,
      country: user.country,
      role,
      allergyIds: user.allergies?.map((a) => a.id) || [],
      email: user.email,
    };
  }

  function highlight(text, query) {
    return highlightParts(text, query).map((part, i) =>
      part.match ? (
        <span key={i} className="text-accent font-semibold">
          {part.text}
        </span>
      ) : (
        part.text
      )
    );
  }

  if (isLoading) return <SkeletonSection title="Users" />;

  if (!users.length)
    return <div className="text-gray-500">No users found.</div>;

  return (
    <div className="space-y-4">
      <input
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        placeholder="Search users..."
        className="w-full px-3 py-2 rounded-lg bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700"
      />
      {filteredUsers.map((u) => (
        <Card key={u.id}>
          <div className="flex justify-between items-center">
            <div>
              <div>
                <span
                  onClick={() => navigate(`/user/${u.id}`)}
                  className="font-semibold text-lg cursor-pointer hover:underline w-fit"
                >{highlight(u.login, search)}
                </span>
              </div>

              {canModify(u) ? (
                <select
                  value={u.role}
                  onChange={(e) =>
                    updateRoleMutation.mutate(
                      buildUpdatePayload(u, e.target.value)
                    )
                  }
                  className="mt-2 text-xs px-2 py-1 rounded bg-gray-100 dark:bg-gray-800"
                >
                  {getAssignableRoles().map((r) => (
                    <option key={r}>{r}</option>
                  ))}
                  <option>{u.role}</option>
                </select>
              ) : (
                <div className="text-xs text-gray-500 mt-1">{u.role}</div>
              )}
            </div>

            <button
              disabled={!canModify(u)}
              onClick={() =>
                confirm("Delete user?") && deleteMutation.mutate(u.id)
              }
              className={`px-3 py-1 rounded-lg text-sm transition
                ${canModify(u)
                  ? "bg-red-500 text-white hover:bg-red-600"
                  : "bg-gray-300 text-gray-500 cursor-not-allowed"
                }`}
            >
              Delete
            </button>
          </div>
        </Card>
      ))}
    </div>
  );
}

/* ================= POSTS ================= */

function PostsTab() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const [search, setSearch] = useState("");

  const { data: posts = [], isLoading } = useQuery({
    queryKey: ["admin-posts"],
    queryFn: getPosts,
  });

  const deleteMutation = useMutation({
    mutationFn: deletePost,
    onSuccess: () => queryClient.invalidateQueries(["admin-posts"]),
  });

  const filteredPosts = posts.filter((p) =>
    p.title.toLowerCase().includes(search.toLowerCase())
  );

  function getAvgRating(post) {
    const feedbacks = post.feedbacks || [];
    if (!feedbacks.length) return 0;

    return Number(
      (
        feedbacks.reduce((sum, f) => sum + (f.rating || 0), 0) /
        feedbacks.length
      ).toFixed(1)
    );
  }

  function highlight(text, query) {
    return highlightParts(text, query).map((part, i) =>
      part.match ? (
        <span key={i} className="text-accent font-semibold">
          {part.text}
        </span>
      ) : (
        part.text
      )
    );
  }

  if (isLoading) return <SkeletonSection title="Recipes" />;

  return (
    <div className="space-y-4">
      <input
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        placeholder="Search recipes..."
        className="w-full px-3 py-2 rounded-lg bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700"
      />

      {filteredPosts.map((p) => {
        const avg = getAvgRating(p);

        const isEdited =
          p.updateDate && p.creationDate
            ? p.updateDate !== p.creationDate
            : false;

        return (
          <Card key={p.id}>
            <div className="flex justify-between items-start">
              <div className="space-y-2">

                {/* TITLE */}
                <div
                  onClick={() => navigate(`/post/${p.id}`)}
                  className="font-semibold cursor-pointer hover:underline w-fit"
                >
                  {highlight(p.title, search)}
                </div>

                {/* AUTHOR */}
                <div
                  onClick={() => navigate(`/user/${p.authorId}`)}
                  className="text-xs text-gray-500 cursor-pointer hover:underline w-fit"
                >
                  by {p.author}
                </div>

                {/* RATING */}
                <div className="flex items-center gap-2">
                  {avg ? (
                    <span className="text-xs px-2 py-1 rounded-full bg-yellow-100 dark:bg-yellow-900 text-yellow-700 dark:text-yellow-300">
                      {avg} ★
                    </span>
                  ) : (
                    <span className="text-xs text-gray-400">
                      No ratings
                    </span>
                  )}
                </div>

                {/* CATEGORIES */}
                <div className="flex flex-wrap gap-2">
                  {p.categories?.map((c) => (
                    <span
                      key={c}
                      className="text-xs px-2 py-1 rounded-full bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300"
                    >
                      {c}
                    </span>
                  ))}
                </div>

                {/* ALLERGIES */}
                <div className="flex flex-wrap gap-2">
                  {p.allergies?.map((a) => (
                    <span
                      key={a}
                      className="text-xs px-2 py-1 rounded-full bg-red-100 dark:bg-red-900 text-red-700 dark:text-red-300"
                    >
                      {a}
                    </span>
                  ))}
                </div>

                {/* DATES */}
                <div className="text-xs text-gray-500 flex gap-2">
                  <span>Created: {p.creationDate}</span>
                  {isEdited && <span>(edited)</span>}
                </div>

              </div>

              {/* DELETE BUTTON */}
              <button
                onClick={() => deleteMutation.mutate(p.id)}
                className="px-3 py-1 bg-red-500 hover:bg-red-600 text-white rounded-lg transition"
              >
                Delete
              </button>
            </div>
          </Card>
        );
      })}
    </div>
  );
}

/* ================= GENERIC CRUD LIST ================= */

function EditableList({
  items,
  createFn,
  updateFn,
  deleteFn,
  queryKey,
  placeholder,
}) {
  const queryClient = useQueryClient();
  const [newName, setNewName] = useState("");
  const [editing, setEditing] = useState(null);
  const [editValue, setEditValue] = useState("");

  const createMutation = useMutation({
    mutationFn: createFn,
    onSuccess: () => {
      queryClient.invalidateQueries([queryKey]);
      setNewName("");
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, name }) => updateFn(id, { name }),
    onSuccess: () => {
      queryClient.invalidateQueries([queryKey]);
      setEditing(null);
    },
  });

  const deleteMutation = useMutation({
    mutationFn: deleteFn,
    onSuccess: () => queryClient.invalidateQueries([queryKey]),
  });

  return (
    <div className="space-y-4">

      {/* CREATE */}
      <div className="flex gap-2">
        <input
          value={newName}
          onChange={(e) => setNewName(e.target.value)}
          placeholder={placeholder}
          className="px-3 py-2 bg-gray-100 dark:bg-gray-800 rounded-lg
           border border-gray-200 dark:border-gray-700 w-full focus:ring-2 focus:ring-accent outline-none"
        />
        <button
          onClick={() => {
            if (!newName.trim()) return;
            createMutation.mutate({ name: newName });
          }}
          className="px-4 bg-green-500 hover:bg-green-600 text-white rounded-lg transition"
        >
          Add
        </button>
      </div>

      {/* LIST */}
      {items.map((item) => (
        <Card key={item.id}>
          <div className="flex justify-between items-center">

            {/* EDIT MODE */}
            {editing === item.id ? (
              <input
                value={editValue}
                onChange={(e) => setEditValue(e.target.value)}
                onBlur={() =>
                  updateMutation.mutate({
                    id: item.id,
                    name: editValue,
                  })
                }
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    updateMutation.mutate({
                      id: item.id,
                      name: editValue,
                    });
                  }
                }}
                autoFocus
                className="px-2 py-1 border rounded w-full bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700"
              />
            ) : (
              <div className="font-medium">{item.name}</div>
            )}

            {/* ACTION MENU */}
            <DropdownMenu>
              {(close) => (
                <div className="flex flex-col">

                  <button
                    onClick={() => {
                      close();
                      setEditing(item.id);
                      setEditValue(item.name);
                    }}
                    className="flex px-3 py-2 text-sm hover:bg-gray-100 dark:hover:bg-gray-800 text-left"
                  >
                    <EditIcon /> Edit
                  </button>

                  <button
                    onClick={() => {
                      close();
                      if (confirm("Delete item?")) {
                        deleteMutation.mutate(item.id);
                      }
                    }}
                    className="flex px-3 py-2 text-sm text-red-600 hover:bg-gray-100 dark:hover:bg-gray-800 text-left"
                  >
                    <DeleteIcon /> Delete
                  </button>

                </div>
              )}
            </DropdownMenu>

          </div>
        </Card>
      ))}
    </div>
  );
}

/* ================= CATEGORIES ================= */

function CategoriesTab() {
  const { data = [], isLoading } = useQuery({
    queryKey: ["categories"],
    queryFn: getCategories,
  });

  if (isLoading) return <SkeletonSection title="Categories" />;

  return (
    <EditableList
      items={data}
      createFn={createCategory}
      updateFn={updateCategory}
      deleteFn={deleteCategory}
      queryKey="categories"
      placeholder="New category"
    />
  );
}

/* ================= ALLERGIES ================= */

function AllergiesTab() {
  const { data = [], isLoading } = useQuery({
    queryKey: ["allergies"],
    queryFn: getAllergies,
  });

  if (isLoading) return <SkeletonSection title="Allergies" />;

  return (
    <EditableList
      items={data}
      createFn={createAllergy}
      updateFn={updateAllergy}
      deleteFn={deleteAllergy}
      queryKey="allergies"
      placeholder="New allergy"
    />
  );
}

/* ================= FEEDBACKS ================= */

function FeedbacksTab() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const [search, setSearch] = useState("");

  const { data: feedbacks = [], isLoading } = useQuery({
    queryKey: ["admin-feedbacks"],
    queryFn: getFeedbacks,
  });

  const deleteMutation = useMutation({
    mutationFn: deleteFeedback,
    onSuccess: () =>
      queryClient.invalidateQueries(["admin-feedbacks"]),
  });

  const filteredFeedbacks = feedbacks.filter((f) =>
    f.content.toLowerCase().includes(search.toLowerCase()) ||
    f.author.toLowerCase().includes(search.toLowerCase()) ||
    String(f.postId).includes(search)
  );

  function highlight(text, query) {
    return highlightParts(text, query).map((part, i) =>
      part.match ? (
        <span key={i} className="text-accent font-semibold">
          {part.text}
        </span>
      ) : (
        part.text
      )
    );
  }

  if (isLoading) return <SkeletonSection title="Feedbacks" />;

  return (
    <div className="space-y-4">
      <input
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        placeholder="Search feedbacks..."
        className="w-full px-3 py-2 rounded-lg bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700"
      />

      {filteredFeedbacks.map((f) => (
        <Card key={f.id}>
          <div className="flex justify-between items-center">
            <div>
              <div>
                <span className="font-semibold">Rating: </span>
                <span className="text-xs px-2 py-1 rounded-full bg-yellow-100 dark:bg-yellow-900 text-yellow-700 dark:text-yellow-300">
                  {f.rating} ★
                </span>
              </div>
              <div className="font-semibold">
                Content: {highlight(f.content, search)}
              </div>
              <div className="text-xs text-gray-500">
                <span
                  onClick={() => navigate(`/post/${f.postId}`)}
                  className="cursor-pointer hover:underline w-fit"
                >Post ID: {f.postId}
                </span> •
                <span
                  onClick={() => navigate(`/user/${f.authorId}`)}
                  className="cursor-pointer hover:underline w-fit"
                > Author: {f.author}
                </span>
              </div>
            </div>

            <button
              onClick={() =>
                confirm("Delete feedback?") &&
                deleteMutation.mutate(f.id)
              }
              className="px-3 py-1 bg-red-500 hover:bg-red-600 text-white rounded-lg transition"
            >
              Delete
            </button>
          </div>
        </Card>
      ))}
    </div>
  );
}