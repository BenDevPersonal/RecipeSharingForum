import { useParams, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { getUserById } from "../api/users";
import { getPosts } from "../api/posts";
import { ErrorMessage } from "../components/ErrorMessage";
import { computeReputation, getBadge } from "../utils/reputation";

export function User() {
    const { id } = useParams();
    const navigate = useNavigate();

    const { data, isLoading, isError, error } = useQuery({
        queryKey: ["user", id],
        queryFn: () => getUserById(id),
    });

    const { data: posts } = useQuery({
        queryKey: ["userPosts", id],
        queryFn: () => getPosts(),
        select: (data) => data.filter((p) => p.authorId === Number(id)),
    });

    if (isLoading) {
        return (
            <div className="max-w-2xl mx-auto px-6 py-10 space-y-6 animate-pulse">
                <div className="h-8 bg-gray-200 dark:bg-gray-700 rounded w-1/2"></div>
                <div className="h-24 bg-gray-200 dark:bg-gray-700 rounded"></div>
            </div>
        );
    }

    if (isError) return <ErrorMessage message={error.message} />;
    if (!data) return <div className="p-10 text-gray-500">User not found</div>;

    const { reputation } = computeReputation(posts || [], data.id);
    const badge = getBadge(reputation);

    return (
        <div className="max-w-2xl mx-auto px-6 py-10 space-y-6">

            <h1 className="text-3xl font-bold text-gray-900 dark:text-gray-100">
                {data.login}
            </h1>

            <div className="bg-white dark:bg-gray-900 p-6 rounded-2xl space-y-3 border border-gray-100 dark:border-gray-800">
                <div className="items-center gap-2 mt-2">
                    <span className="font-semibold">Badge: </span>
                    <div className="inline-flex w-fit text-xs px-2 py-1 rounded-full 
                        text-green-700 dark:text-green-300 bg-green-100 dark:bg-green-900
                        border border-green-200 dark:border-green-300">
                        {badge}
                    </div>

                    <div className="text-xs text-gray-500 py-1">
                        Reputation: {reputation}
                    </div>
                </div>
                <p><span className="font-semibold">Country:</span> {data.country}</p>
                <p><span className="font-semibold">Role:</span> {data.role}</p>

                <div>
                    <p className="font-semibold">Allergies:</p>
                    {data.allergies.length ? (
                        <div className="flex gap-2 flex-wrap mt-2">
                            {data.allergies.map((a, i) => (
                                <span key={i} className="px-3 py-1 bg-gray-200 dark:bg-gray-800 rounded-full text-sm">
                                    {a}
                                </span>
                            ))}
                        </div>
                    ) : (
                        <p className="text-gray-500">None</p>
                    )}
                </div>
            </div>

            <div className="space-y-4">
                <h2 className="text-xl font-semibold text-gray-900 dark:text-gray-100">
                    Posts
                </h2>

                {!posts || posts.length === 0 ? (
                    <p className="text-gray-500 dark:text-gray-400">
                        This user has no posts.
                    </p>
                ) : (
                    posts.map((post) => {
                        const avg =
                            post.feedbacks && post.feedbacks.length
                                ? Number(
                                    (
                                        post.feedbacks.reduce(
                                            (sum, f) => sum + (f.rating || 0),
                                            0
                                        ) / post.feedbacks.length
                                    ).toFixed(1)
                                )
                                : 0;

                        const isEdited =
                            post.updateDate && post.creationDate
                                ? post.updateDate !== post.creationDate
                                : false;

                        return (
                            <div
                                key={post.id}
                                onClick={() => navigate(`/post/${post.id}`)}
                                className="p-4 rounded-2xl bg-white dark:bg-gray-900 shadow-soft hover:shadow-md transition cursor-pointer space-y-2"
                            >
                                <div className="font-semibold text-gray-900 dark:text-gray-100">
                                    {post.title}
                                </div>

                                <div className="text-sm text-gray-500">
                                    by {post.author}
                                </div>

                                <div className="flex items-center gap-2">
                                    {avg ? (
                                        <span className="text-sm px-3 py-1 rounded-full bg-yellow-100 dark:bg-yellow-900 text-yellow-700 dark:text-yellow-300">
                                            {avg} ★
                                        </span>
                                    ) : (
                                        <span className="text-sm text-gray-400">No rating</span>
                                    )}
                                </div>

                                <div className="flex flex-wrap gap-2">
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

                                <div className="text-xs text-gray-500 flex gap-2">
                                    <span>Created: {post.creationDate}</span>
                                    {isEdited && <span>(edited)</span>}
                                </div>
                            </div>
                        );
                    })
                )}
            </div>

        </div>
    );
}