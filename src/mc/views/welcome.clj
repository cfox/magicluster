(ns mc.views.welcome
  (:require [mc.views.common :as common]
            [noir.content.pages :as pages])
  (:use (mc db cards util metagame)
        noir.core
        hiccup.core
        hiccup.page-helpers))

(defpage "/welcome" []
         (common/layout
          [:p "Welcome to magicluster"]))

(defpartial tournament-link [{:keys [id name date]}]
  (let [formatter (java.text.SimpleDateFormat. "MM/dd")]
    [:a {:href (str "/tournament/" id)}
     (.format formatter date) "  " name]))

(defpartial tournament-summary-list-item [ts]
  [:li.tournamentSummary (tournament-link ts)])

(defpartial tournament-summary-list [tss]
  [:ul.tournamentSummaries
   (map tournament-summary-list-item tss)])

(defpage "/tournaments" []
  (let [tss (load-tournament-summaries)]
    (tournament-summary-list tss)))

(defn classify
  [card-name]
  (let [card (get-card card-name)
        type (:type card)]
    (cond
     (re-matches #".*Land.*" type) :land
     (re-matches #".*Creature.*" type) :creature
     true :other)))

(defpartial deck-listing-chunk
  [slots count label]
  [:ul
   (map #(vector :li (str (:count %) " " (:card %))) slots)]
  [:p (str count " " label)])

(defpartial deck-listing
  [deck]
  (let [count-cards (fn [slots] (reduce + (map :count slots)))
        main-count (count-cards (:main deck))
        classified (group-by #(classify (:card %)) (:main deck))
        lands (:land classified)
        land-count (count-cards lands)
        creatures (:creature classified)
        creature-count (count-cards creatures)
        other (:other classified)
        other-count (count-cards other)
        sideboard (:sideboard deck)
        sideboard-count (count-cards sideboard)]
    [:table
     [:tr [:td {:colspan 2} (str "Main Deck (" main-count " cards)")]]
     [:tr
      [:td {:valign :top}
       (deck-listing-chunk lands land-count "lands")
       (deck-listing-chunk creatures creature-count "creatures")]
      [:td {:valign :top}
       (deck-listing-chunk other other-count "other spells")
       [:p "Sideboard"]
       (deck-listing-chunk sideboard sideboard-count "sideboard cards")]
      ]]))

(defpartial deck-listing-full
  [deck tournament]
  (let [results (:results tournament)
        result (get (zipmap (map :player results) results) (:player deck))
        result-summary (str (get-ordinal (:placing result)) " place "
                            "(" (:points result) " points)")
        formatter (java.text.SimpleDateFormat. "MM/dd/yyyy")
        tournament-summary (str (:name tournament) " #" (:id tournament)
                                " on " (.format formatter (:date tournament)))]
    [:table {:bgcolor "#E0E0E0" :border 5}
     [:tr
      [:td  {:colspan 2} (str (:player deck) " " result-summary)]]
     [:tr
      [:td  {:colspan 2} tournament-summary]]
     [:tr
      [:td (deck-listing deck)]
      [:td "rollover here"]]]
    ))

(defpage "/tournament/:id" {tournament-id :id}
  (let [t (load-tournament tournament-id)]
    (map deck-listing-full (:decks t) (repeat t))))

(defn hash-cardset
  [cards]
  (hash (zipmap (map :card cards) (map :count cards))))

(defpage "/tournament/:id/metagame"
  {tournament-id :id}
  (let [t (load-tournament (.toString tournament-id))
        dtb (decks-to-beat [t])
        dtb-hashes (map hash-cardset dtb)
        deck-hash-map (zipmap (map #(hash-cardset (:main %)) (:decks t))
                              (:decks t))
        decks (map (partial get deck-hash-map) dtb-hashes)]
    (map deck-listing-full decks (repeat t))))
