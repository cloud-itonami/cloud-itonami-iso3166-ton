(ns marketentry.registry-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.registry :as registry]))

(deftest engagement-fee-recompute
  (let [e {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 860000.0}]
    (is (== 860000.0 (registry/compute-engagement-fee e)))
    (is (true? (registry/engagement-fee-matches-claim? e))))
  (let [bad {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 999000.0}]
    (is (false? (registry/engagement-fee-matches-claim? bad)))))

(deftest register-draft-and-submit
  (let [d (registry/register-draft "eng-1" "TON" 0)
        s (registry/register-submit "eng-1" "TON" 0)]
    (is (= "TON-DFT-000000" (get d "draft_number")))
    (is (= "TON-SUB-000000" (get s "submit_number")))
    (is (nil? (get-in d ["certificate" "proof"])))
    (is (= "draft-unsigned" (get-in s ["certificate" "status"])))))

(deftest register-requires-ids
  (is (thrown? Exception (registry/register-draft "" "TON" 0)))
  (is (thrown? Exception (registry/register-submit "eng-1" "" 0))))

(deftest reserved-activity-is-an-absolute-bar
  (testing "the Foreign Investment Regulations 2021's own gazetted Reserved List (Schedule 1) bars an overseas person outright, regardless of ownership composition or capital"
    (is (true? (registry/reserved-activity? :bread-manufacture)))
    (is (true? (registry/reserved-activity? :traditional-tongan-cultural-activities)))
    (is (false? (registry/foreign-investment-eligible? :bread-manufacture 1.0 10000000)))
    (is (false? (registry/foreign-investment-eligible? :bread-manufacture nil nil)))))

(deftest unlisted-sector-has-no-s5-gate
  (testing "a business sector outside both the Reserved and Restricted Lists is not barred by s.5 at all"
    (is (false? (registry/reserved-activity? :general-wholesale-and-distribution)))
    (is (nil? (registry/restricted-activity-conditions :general-wholesale-and-distribution)))
    (is (true? (registry/foreign-investment-eligible? :general-wholesale-and-distribution nil nil)))))

(deftest restricted-activity-conditions-satisfied
  (testing "an item requiring BOTH a Tongan-equity percentage AND a minimum paid-up capital (Schedule 2 item 1 and item 3 shape)"
    (is (true? (registry/restricted-activity-conditions-satisfied?
                :squash-pandanus-kava-and-forestry-growing 0.30 250000)))
    (is (true? (registry/restricted-activity-conditions-satisfied?
                :squash-pandanus-kava-and-forestry-growing 0.50 300000)))
    (is (false? (registry/restricted-activity-conditions-satisfied?
                 :squash-pandanus-kava-and-forestry-growing 0.10 300000))
        "equity share below the 30% threshold fails even when capital is sufficient")
    (is (false? (registry/restricted-activity-conditions-satisfied?
                 :squash-pandanus-kava-and-forestry-growing 0.30 100000))
        "capital below the $250,000 threshold fails even when equity is sufficient"))
  (testing "an item requiring ONLY a minimum paid-up capital, with NO Tongan-equity condition at all (Schedule 2 item 2's own text)"
    (is (true? (registry/restricted-activity-conditions-satisfied?
                :food-beverage-tobacco-retail 0.0 250000))
        "zero Tongan equity is fine -- the Regulations' own text states no equity condition for this item")
    (is (false? (registry/restricted-activity-conditions-satisfied?
                 :food-beverage-tobacco-retail nil 100000))))
  (testing "an unlisted sector has no restricted-activity conditions to satisfy"
    (is (nil? (registry/restricted-activity-conditions-satisfied?
               :general-wholesale-and-distribution 1.0 10000000)))))

(deftest foreign-investment-violation-is-entity-scope-gated
  (testing "an engagement NOT seeking a FIRC is never flagged, even for a reserved/ineligible sector"
    (is (false? (registry/foreign-investment-violation?
                 {:seeks-firc? false :business-sector :bread-manufacture
                  :tongan-equity-pct 0.0 :paid-up-capital nil}))))
  (testing "an engagement seeking a FIRC for a Reserved-List activity is always ineligible"
    (is (true? (registry/foreign-investment-violation?
                {:seeks-firc? true :business-sector :bread-manufacture
                 :tongan-equity-pct 1.0 :paid-up-capital 10000000}))))
  (testing "an engagement seeking a FIRC for a Restricted-List activity whose own conditions its declared facts do not satisfy"
    (is (true? (registry/foreign-investment-violation?
                {:seeks-firc? true :business-sector :squash-pandanus-kava-and-forestry-growing
                 :tongan-equity-pct 0.10 :paid-up-capital 300000}))))
  (testing "an engagement seeking a FIRC for a Restricted-List activity whose own conditions ARE satisfied -- not flagged"
    (is (false? (registry/foreign-investment-violation?
                 {:seeks-firc? true :business-sector :squash-pandanus-kava-and-forestry-growing
                  :tongan-equity-pct 0.30 :paid-up-capital 250000}))))
  (testing "an engagement seeking a FIRC for an unlisted sector is never flagged by this check"
    (is (false? (registry/foreign-investment-violation?
                 {:seeks-firc? true :business-sector :general-wholesale-and-distribution
                  :tongan-equity-pct nil :paid-up-capital nil})))))
