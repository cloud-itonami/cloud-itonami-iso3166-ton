(ns statute.facts-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [statute.facts :as facts]))

(deftest ton-has-spec-basis
  (let [sb (facts/spec-basis "TON")]
    (is (= 2 (count sb)))
    (is (every? #(str/starts-with? (:statute/url %) "https://") sb))
    (is (every? :statute/law-number sb))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["TON" "JPN" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["ATL" "JPN"] (:missing-jurisdictions c)))))

(deftest by-topic-filters
  (is (= ["ton.companies-act-1995"]
         (mapv :statute/id (facts/by-topic "TON" :corporate-governance))))
  (is (= ["ton.immigration-act-cap-62"]
         (mapv :statute/id (facts/by-topic "TON" :labor))))
  (is (empty? (facts/by-topic "TON" :data-protection))
      "no data-protection statute independently confirmed this iteration -- honestly absent, see namespace docstring")
  (is (empty? (facts/by-topic "ATL" :labor))))
