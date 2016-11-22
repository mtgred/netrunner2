(ns netrunner.cards
  (:require [netrunner.core :refer :all]
            [netrunner.macros :refer [fx req]]))

(defcard "Akamatsu Mem Chip"
  :in-play [:memory 1])

(defcard "Astrolab"
  :in-play [:memory 1])

(defcard "Access to Globalsec"
  :in-play [:link 1])

(defcard "Anonymous Tip"
  :effect (fx (draw 3)))

(defcard "Beanstalk Royalties"
  :effect (fx (gain :credit 3)))

(defcard "Big Brother"
  :req (req tagged)
  :effect (fx (gain :runner :tag 2)))

(defcard "Biotic Labor"
  :effect (fx (gain :click 2)))

(defcard "Blue Level Clearance"
  :effect (fx (gain :credit 5) (draw 2)))

(defcard "Box-E"
  :in-play [:memory 2 :hand-size-modification 2])

(defcard "Corporate Scandal"
  :effect (fx (gain :corp :bad-publicity 1))
  :leave-play (fx (lose :corp :bad-publicity 1)))

(defcard "Closed Accounts"
  :req (req tagged)
  :effect (fx (lose :runner :credit :all)))

(defcard "Cyberdex Trial"
  :effect (fx (purge)))

(defcard "Desperado"
  :in-play [:memory 1])

(defcard "Diesel"
  :effect (fx (draw 3)))

(defcard "Doppelgänger"
  :in-play [:memory 1])

(defcard "Dyson Mem Chip"
  :in-play [:memory 1 :link 1])

(defcard "Easy Mark"
  :effect (fx (gain :credit 3)))

(defcard "Fisk Investment Seminar"
  :effect (fx (draw 3) (draw :runner 3)))

(defcard "Forger"
  :in-play [:link 1])

(defcard "Game Day"
  :effect (fx (draw (- (:max-hand-size runner) (count (:hand runner))))))

(defcard "Green Level Clearance"
  :effect (fx (gain :credit 3) (draw)))

(defcard "Grimoire"
  :in-play [:memory 2])

(defcard "Hedge Fund"
  :effect (fx (gain :credit 9)))

(defcard "Lawyer Up"
  :effect (fx (lose :tag 2) (draw 3)))

(defcard "Lucky Find"
  :effect (fx (gain :credit 9)))

(defcard "Medical Research Fundraiser"
  :effect (fx (gain :credit 8) (gain :runner :credit 3)))

(defcard "Public Sympathy"
  :in-play [:hand-size-modification 2])

(defcard "Quality Time"
  :effect (fx (draw 5)))

(defcard "Restructure"
  :effect (fx (gain :credit 15)))

(defcard "Subliminal Messaging"
  :effect (fx (gain :credit 1 :click 1)))

(defcard "Sure Gamble"
  :effect (fx (gain :credit 9)))

(defcard "The Toolbox"
  :in-play [:memory 2 :link 2]
  :recuring 2)

(defcard "Violet Level Clearance"
  :effect (fx (gain 8) (draw 4)))

(defcard "Witness Tampering"
  :effect (fx (lose :bad-publicity 2)))
