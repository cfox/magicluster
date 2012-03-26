(ns mc.db
  (:use somnium.congomongo
        [somnium.congomongo.config :only [*mongo-config*]]))

(def default-url "mongodb://heroku:69ff5fb36833ac64e41bad59bc0a5abe@staff.mongohq.com:10096/app3441684")

(defn split-mongo-url
  [url]
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)]
    (when (.find matcher)
      (zipmap [:match :user :pass :host :port :db] (re-groups matcher)))))

(defn maybe-init
  []
  (when (not (connection? *mongo-config*))
    (let [mongo-url (or (System/getenv "MONGOHQ_URL") default-url)
          config (split-mongo-url mongo-url)]
      (println "Initializing mongo @ " mongo-url)
      (mongo! :db (:db config)
              :host (:host config)
              :port (Integer. (:port config)))
      (authenticate (:user config) (:pass config))
      (or (collection-exists? :tournaments)
          (create-collection! :tournaments))
      (add-index! :tournaments [:id] :unique true))))

(defn save-tournament
  [t]
  (maybe-init)
  (insert! :tournaments t))

(defn load-tournament
  [id]
  (maybe-init)
  (fetch-one :tournaments :where {:id id}))
