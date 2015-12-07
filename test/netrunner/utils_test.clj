(ns netrunner.utils-test
  (:use expectations)
  (:require [netrunner.init :refer [card-data]]
            [netrunner.utils :refer [has?]]))

(let [card (card-data "Blue Level Clearance")]
  (expect true (has? card :subtype "Double"))
  (expect true (has? card :subtype "Transaction"))
  (expect false (has? card :subtype "Priority")))
