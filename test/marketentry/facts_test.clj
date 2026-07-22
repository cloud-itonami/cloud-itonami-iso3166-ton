(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest ton-has-spec-basis
  (let [sb (facts/spec-basis "TON")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/corporate-number-spec-basis "TON")))
    (is (some? (facts/foreign-investment-spec-basis "TON")))))

(deftest ton-rep-spec-basis-is-populated
  (testing "the Foreign Investment Act 2020's own ss.14(1)(d)/17(1)(d) representative-in-Tonga requirement is confirmed"
    (is (some? (facts/rep-spec-basis "TON")))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "TON")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "TON" all)))
    (is (not (facts/required-evidence-satisfied? "TON" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["TON" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))

(deftest foreign-investment-spec-basis-criteria
  (let [fi (facts/foreign-investment-spec-basis "TON")]
    (is (contains? (get-in fi [:foreign-investment-criteria :reserved-activities]) :bread-manufacture))
    (is (= 0.30 (get-in fi [:foreign-investment-criteria :restricted-activities
                            :squash-pandanus-kava-and-forestry-growing :min-tongan-equity-pct])))
    (is (nil? (get-in fi [:foreign-investment-criteria :restricted-activities
                         :food-beverage-tobacco-retail :min-tongan-equity-pct])))
    (is (= 250000 (get-in fi [:foreign-investment-criteria :restricted-activities
                              :food-beverage-tobacco-retail :min-paid-up-capital])))))
