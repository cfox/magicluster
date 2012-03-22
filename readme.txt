; SLIME 2009-10-15
user> (use 'mc.in)
nil
user> (def t (extract-one-tournament))
#'user/t
user> (:main (first (:decks t)))
(#mc.magic.Slot{:card "Arid Mesa", :count 1} #mc.magic.Slot{:card "Celestial Colonnade", :count 2} #mc.magic.Slot{:card "Hallowed Fountain", :count 1} #mc.magic.Slot{:card "Island", :count 4} #mc.magic.Slot{:card "Misty Rainforest", :count 2} #mc.magic.Slot{:card "Mountain", :count 1} #mc.magic.Slot{:card "Sacred Foundry", :count 1} #mc.magic.Slot{:card "Scalding Tarn", :count 4} #mc.magic.Slot{:card "Steam Vents", :count 2} #mc.magic.Slot{:card "Sulfur Falls", :count 3} #mc.magic.Slot{:card "Tectonic Edge", :count 2} #mc.magic.Slot{:card "Delver of Secrets", :count 4} #mc.magic.Slot{:card "Snapcaster Mage", :count 4} #mc.magic.Slot{:card "Vendilion Clique", :count 2} #mc.magic.Slot{:card "Isochron Scepter", :count 4} #mc.magic.Slot{:card "Lightning Bolt", :count 4} #mc.magic.Slot{:card "Lightning Helix", :count 4} #mc.magic.Slot{:card "Magma Jet", :count 3} #mc.magic.Slot{:card "Path to Exile", :count 4} #mc.magic.Slot{:card "Serum Visions", :count 4} #mc.magic.Slot{:card "Spell Pierce", :count 2} #mc.magic.Slot{:card "Spell Snare", :count 2})
