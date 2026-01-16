--standard
update place_voiture_cat set prix = 50000 where id_place_voiture= 1 and id_categorie=1; --standard adulte
update  place_voiture_cat set prix = 40000 where id_place_voiture= 1 and id_categorie=2;--standard enfant


--vip   
update place_voiture_cat set prix = 70000 where id_place_voiture= 3 and id_categorie=1; --vip adulte
update  place_voiture_cat set prix = 65000 where id_place_voiture= 3 and id_categorie=2;--vip enfant


--premium
update place_voiture_cat set prix = 60000 where id_place_voiture= 2 and id_categorie=1; --premium adulte
update  place_voiture_cat set prix = 50000 where id_place_voiture= 2 and id_categorie=2;--premium




update place_voiture_cat set prix = 20 where id_categorie= 3; 