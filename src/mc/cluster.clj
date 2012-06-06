(ns mc.cluster)

(defn coerce-coll
  "Force scalars into one-dim vectors."
  [x]
  (if (coll? x) x [x]))

(defn fractional-distance
  "Obtain a fractional distance metric between x and y
   using an Lf norm for f in (0,1)."
  [f x y]
  (let [distance (fn [xi yi] (Math/pow (Math/abs (- xi yi)) f))]
    (Math/pow (reduce + (map distance (coerce-coll x) (coerce-coll y)))
              (/ 1 f))))

(defn fractional-distance-fn
  "Build a distance fn for a particular value of f."
  [f]
  (partial fractional-distance f))

(defn memoized-fractional-distance-fn
  "Build a memoized distance fn."
  [f]
  (let [mem-fn (memoize (fractional-distance-fn f))]
    (fn [x y] (apply mem-fn (sort [x y])))))

(defn closest
  "Find the closesd medoid to a given point."
  [distance medoids point]
  (first (sort-by (partial distance point) medoids)))

(defn point-groups
  "Group all points by the closest medoid.  Returns a map of medoid -> points."
  [distance medoids points]
  (group-by (partial closest distance medoids) points))

(defn configuration-cost
  "Return the sum of the distances between each point and the medoid."
  [distance points medoid]
  (reduce + (map (partial distance medoid) points)))

(defn find-best-medoid
  "Given a set of points, find the medoid which minimizes the cost."
  [distance points]
  (first (sort-by (partial configuration-cost distance points) points)))

(defn new-medoids
  "Given a map of point-groups representing the current iteration of medoids
   and clusters, return a new set of medoids that minimizes the total cost."
  [distance point-groups]
  (map (partial find-best-medoid distance) (vals point-groups)))

(defn iterate-medoids
  "Given a distance function and set of points, returns a function which
   takes a set of medoids and iterates them pam-style."
  [distance points]
  (fn [medoids] (new-medoids distance (point-groups distance medoids points))))

(defn groups
  "Get the clusters in points around medoids using distance."
  [distance points medoids]
  (vals (point-groups distance medoids points)))

(defn take-while-unstable
  ([sq] (lazy-seq (if-let [sq (seq sq)]
                    (cons (first sq)
                          (take-while-unstable (rest sq) (first sq))))))
  ([sq last] (lazy-seq (if-let [sq (seq sq)]
                         (if (= (first sq) last) '()
                             (take-while-unstable sq))))))

(defn k-medoids
  "k-medoids PAM implementation.
  f is used to generate a fractional distance metric.
  Returns a map of medoids to clusters."
  [points k f]
  (let [distance (memoized-fractional-distance-fn f)
        initial-medoids (take k (shuffle points))
        medoids (last (take-while-unstable
                       (iterate (iterate-medoids distance points)
                                initial-medoids)))
        clusters (groups distance points medoids)]
    (zipmap medoids clusters)))

(defn average-dissimilarity
  "Average dissimilarity between a point and a cluster using distance."
  [distance point cluster]
  (let [total-distance (reduce + (map (partial distance point) cluster))]
    (/ total-distance (count cluster))))

(defn point-silhouette
  "s(i) for a single point."
  [distance cluster neighbors point]
  (let [ai (average-dissimilarity distance point cluster)
        bi (first (sort (map (partial average-dissimilarity distance point)
                             neighbors)))]
    (/ (- bi ai) (Math/max ai bi))))

(defn cluster-silhouette
  "Average s(i) for all the points in a cluster."
  [distance cluster neighbors]
  (let [si-total
        (reduce + (map (partial point-silhouette distance cluster neighbors)
                       cluster))]
    (/ si-total (count cluster))))

(defn silhouette
  "Average s(i) for an entire dataset."
  [distance clusters]
  (let [clustered-point-silhouettes
        (for [cluster clusters
              :let [neighbors (remove #(= cluster %) clusters)]]
          (map (partial point-silhouette distance cluster neighbors) cluster))
        point-silhouettes (flatten clustered-point-silhouettes)]
    (/ (reduce + point-silhouettes) (count point-silhouettes))))
