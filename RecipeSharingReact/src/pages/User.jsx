import { useParams, useNavigate } from "react-router-dom";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { getUserById, getMe } from "../api/users";
import { getPosts } from "../api/posts";
import { getUserSetting } from "../api/settings";
import { ErrorMessage } from "../components/ErrorMessage";
import { computeReputation, getBadge } from "../utils/reputation";
import {
    followUser,
    unfollowUser,
    isFollowingUser,
    getFollowerCount,
} from "../api/follows";
import {
    blacklistUser,
    unblacklistUser,
    isBlacklistedUser,
} from "../api/blacklist";
import { useAuth } from "../context/useAuth";

export function User() {
    const { id } = useParams();
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const { isAuth, token } = useAuth();

    // USER
    const {
        data,
        isLoading,
        isError,
        error,
    } = useQuery({
        queryKey: ["user", id],
        queryFn: () => getUserById(id),
        enabled: !!id,
    });

    // POSTS
    const { data: posts = [] } = useQuery({
        queryKey: ["userPosts", id],
        queryFn: getPosts,
        select: (data) => data.filter((p) => p.authorId === Number(id)),
        enabled: !!id,
    });

    // ME
    const { data: me } = useQuery({
        queryKey: ["me"],
        queryFn: getMe,
    });

    // SETTINGS (important for visibility logic)
    const { data: settings } = useQuery({
        queryKey: ["settings", id],
        queryFn: () => getUserSetting(id),
        enabled: !!id,
    });

    // RELATIONSHIP STATE
    const { data: isFollowing = false } = useQuery({
        queryKey: ["isFollowing", id],
        queryFn: () => isFollowingUser(id),
        enabled: !!id,
    });

    const { data: isBlacklisted = false } = useQuery({
        queryKey: ["isBlacklisted", id],
        queryFn: () => isBlacklistedUser(id),
        enabled: !!id,
    });

    // FOLLOWER COUNT
    const { data: followerCount = 0 } = useQuery({
        queryKey: ["followers", id],
        queryFn: () => getFollowerCount(id),
        enabled: !!id,
    });

    const isMe = me?.id === Number(id);

    // MUTATIONS
    const followMutation = useMutation({
        mutationFn: followUser,
        onMutate: async () => {
            await queryClient.cancelQueries({ queryKey: ["isFollowing", id] });
            const prev = queryClient.getQueryData(["isFollowing", id]);
            queryClient.setQueryData(["isFollowing", id], true);
            return { prev };
        },
        onError: (_err, _vars, ctx) => {
            queryClient.setQueryData(["isFollowing", id], ctx?.prev);
        },
        onSuccess: () => {
            queryClient.setQueryData(["followers", id], (old = 0) => old + 1);
        },
    });

    const unfollowMutation = useMutation({
        mutationFn: unfollowUser,
        onMutate: async () => {
            await queryClient.cancelQueries({ queryKey: ["isFollowing", id] });
            const prev = queryClient.getQueryData(["isFollowing", id]);
            queryClient.setQueryData(["isFollowing", id], false);
            return { prev };
        },
        onError: (_err, _vars, ctx) => {
            queryClient.setQueryData(["isFollowing", id], ctx?.prev);
        },
        onSuccess: () => {
            queryClient.setQueryData(["followers", id], (old = 0) =>
                Math.max(0, old - 1)
            );
        },
    });

    const blacklistMutation = useMutation({
        mutationFn: blacklistUser,
        onMutate: async () => {
            const prevFollow = queryClient.getQueryData(["isFollowing", id]);
            const prevBlock = queryClient.getQueryData(["isBlacklisted", id]);

            queryClient.setQueryData(["isBlacklisted", id], true);
            queryClient.setQueryData(["isFollowing", id], false);

            return { prevFollow, prevBlock };
        },
        onError: (_err, _vars, ctx) => {
            queryClient.setQueryData(["isBlacklisted", id], ctx?.prevBlock);
            queryClient.setQueryData(["isFollowing", id], ctx?.prevFollow);
        },
        onSuccess: () => {
            queryClient.setQueryData(["followers", id], (old = 0) =>
                Math.max(0, old - 1)
            );
        },
    });

    const unblacklistMutation = useMutation({
        mutationFn: unblacklistUser,
        onMutate: async () => {
            const prev = queryClient.getQueryData(["isBlacklisted", id]);
            queryClient.setQueryData(["isBlacklisted", id], false);
            return { prev };
        },
        onError: (_err, _vars, ctx) => {
            queryClient.setQueryData(["isBlacklisted", id], ctx?.prev);
        },
    });

    // LOADING / ERROR
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

    const { reputation } = computeReputation(posts, data.id);
    const badge = getBadge(reputation);

    // SETTINGS FALLBACK (matches Profile logic)
    const visibility = settings
        ? {
              showCountry: settings.showCountryOnProfile,
              showAllergy: settings.showAllergyOnProfile,
          }
        : {
              showCountry: true,
              showAllergy: true,
          };

    return (
        <div className="max-w-2xl mx-auto px-6 py-10 space-y-6">

            <h1 className="text-3xl font-bold text-gray-900 dark:text-gray-100">
                {data.login}
            </h1>

            {/* ACTION BUTTONS */}
            {!isMe && isAuth && (
                <div className="flex gap-3">
                    <button
                        disabled={
                            followMutation.isPending ||
                            unfollowMutation.isPending ||
                            isBlacklisted
                        }
                        onClick={() =>
                            isFollowing
                                ? unfollowMutation.mutate(data.id)
                                : followMutation.mutate(data.id)
                        }
                        className={`px-4 py-2 rounded-xl text-white transition ${
                            isBlacklisted
                                ? "bg-gray-500 cursor-not-allowed"
                                : isFollowing
                                ? "bg-gray-500 hover:bg-gray-600"
                                : "bg-accent hover:opacity-90"
                        } disabled:opacity-50`}
                    >
                        {isFollowing ? "Unfollow" : "Follow"}
                    </button>

                    <button
                        disabled={
                            blacklistMutation.isPending ||
                            unblacklistMutation.isPending
                        }
                        onClick={() =>
                            isBlacklisted
                                ? unblacklistMutation.mutate(data.id)
                                : blacklistMutation.mutate(data.id)
                        }
                        className={`px-4 py-2 rounded-xl text-white transition ${
                            isBlacklisted
                                ? "bg-gray-600 hover:bg-gray-700"
                                : "bg-red-500 hover:opacity-90"
                        } disabled:opacity-50`}
                    >
                        {isBlacklisted ? "Unblacklist" : "Blacklist"}
                    </button>
                </div>
            )}

            {/* USER INFO */}
            <div className="bg-white dark:bg-gray-900 p-6 rounded-2xl space-y-4 shadow-soft">

                <div className="text-left space-y-1">
                    <div className="inline-flex w-fit text-xs px-2 py-1 rounded-full text-green-700 dark:text-green-300 bg-green-100 dark:bg-green-900 border border-green-200 dark:border-green-300">
                        {badge}
                    </div>

                    <div className="text-xs text-gray-500">
                        Reputation: {reputation}
                    </div>
                </div>

                <p><span className="font-semibold">Followers:</span> {followerCount}</p>

                {visibility.showCountry && (
                    <p><span className="font-semibold">Country:</span> {data.country}</p>
                )}

                <p><span className="font-semibold">Role:</span> {data.role}</p>

                {visibility.showAllergy && (
                    <div>
                        <p className="font-semibold">Allergies</p>

                        {data.allergies?.length ? (
                            <div className="flex gap-2 flex-wrap mt-2">
                                {data.allergies.map((a, i) => (
                                    <span
                                        key={i}
                                        className="px-3 py-1 bg-gray-100 dark:bg-gray-800 rounded-full text-sm"
                                    >
                                        {a}
                                    </span>
                                ))}
                            </div>
                        ) : (
                            <p className="text-gray-500">-</p>
                        )}
                    </div>
                )}
            </div>

            {/* POSTS */}
            <div className="space-y-4">
                <h2 className="text-xl font-semibold text-gray-900 dark:text-gray-100">
                    Posts
                </h2>

                {!posts.length ? (
                    <p className="text-gray-500 dark:text-gray-400">
                        This user has no posts.
                    </p>
                ) : (
                    posts.map((post) => {
                        const avg =
                            post.feedbacks?.length
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
                            post.updateDate &&
                            post.creationDate &&
                            post.updateDate !== post.creationDate;

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
                                        <span className="text-sm text-gray-400">
                                            No rating
                                        </span>
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