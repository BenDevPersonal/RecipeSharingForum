export function Skeleton({ className = "" }) {
    return (
        <div
            className={`animate-pulse bg-gray-200 dark:bg-gray-800 rounded ${className}`}
        />
    );
}

export function SkeletonSection({ title }) {
    return (
        <div className="space-y-4">
            <h2 className="text-xl font-semibold">{title}</h2>

            <div className="space-y-3">
                {Array.from({ length: 3 }).map((_, i) => (
                    <div
                        key={i}
                        className="p-4 rounded-2xl bg-white dark:bg-gray-900 shadow-soft space-y-3"
                    >
                        <Skeleton className="h-4 w-2/3" />
                        <Skeleton className="h-3 w-1/3" />
                        <Skeleton className="h-6 w-24 rounded-full" />
                        <div className="flex gap-2">
                            <Skeleton className="h-5 w-16 rounded-full" />
                            <Skeleton className="h-5 w-16 rounded-full" />
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}