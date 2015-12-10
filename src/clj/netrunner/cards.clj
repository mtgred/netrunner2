(ns netrunner.cards
  (:require [netrunner.core :refer :all]
            [netrunner.macros :refer [effect req]]))

(defcard "Akamatsu Mem Chip"
  :effect (effect (gain :memory 1))
  :leave-play (effect (lose :memory 1)))

(defcard "Access to Globalsec"
  :effect (effect (gain :link 1))
  :leave-play (effect (lose :link 1)))

(defcard "Anonymous Tip"
  :effect (effect (draw 3)))

(defcard "Beanstalk Royalties"
  :effect (effect (gain :credit 3)))

(defcard "Big Brother"
  :req (req tagged)
  :effect (effect (gain :runner :tag 2)))

(defcard "Biotic Labor"
  :effect (effect (gain :click 2)))

(defcard "Blue Level Clearance"
  :effect (effect (gain :credit 5) (draw 2)))

(defcard "Closed Accounts"
  :req (req tagged)
  :effect (effect (lose :runner :credit :all)))

(defcard "Cyberdex Trial"
  :effect (effect (purge)))

(defcard "Diesel"
  :effect (effect (draw 3)))

(defcard "Dyson Mem Chip"
  :effect (effect (gain :memory 1 :link 1))
  :leave-play (effect (lose :memory 1 :link 1)))

(defcard "Easy Mark"
  :effect (effect (gain :credit 3)))

(defcard "Fisk Investment Seminar"
  :effect (effect (draw 3) (draw :runner 3)))

(defcard "Game Day"
  :effect (effect (draw (- (:max-hand-size runner) (count (:hand runner))))))

(defcard "Green Level Clearance"
  :effect (effect (gain :credit 3) (draw)))

(defcard "Hedge Fund"
  :effect (effect (gain :credit 9)))

(defcard "Lawyer Up"
  :effect (effect (lose :tag 2) (draw 3)))

(defcard "Lucky Find"
  :effect (effect (gain :credit 9)))

(defcard "Medical Research Fundraiser"
  :effect (effect (gain :credit 8) (gain :runner :credit 3)))

(defcard "Quality Time"
  :effect (effect (draw 5)))

(defcard "Restructure"
  :effect (effect (gain :credit 15)))

(defcard "Subliminal Messaging"
  :effect (effect (gain :credit 1 :click 1)))

(defcard "Sure Gamble"
  :effect (effect (gain :credit 9)))

(defcard "Witness Tampering"
  :effect (effect (lose :bad-publicity 2)))
