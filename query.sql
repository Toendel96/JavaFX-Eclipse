use kino; 

SELECT v_visningnr, v_filmnr, v_pris, v_dato, v_starttid, HOUR(CURTIME()) - HOUR(v_starttid) AS Dato
FROM tblvisning, tblbillett, tblplass
WHERE v_visningnr = b_visningsnr
	AND v_kinosalnr = p_kinosalnr
		AND HOUR(CURTIME()) - HOUR(v_starttid) 
GROUP BY v_visningnr, v_filmnr, v_kinosalnr;
    
SELECT *
FROM tbllogin;
    
SELECT *
FROM tblvisning;

SELECT *
FROM tblbillett;

SELECT *
FROM tblplass;