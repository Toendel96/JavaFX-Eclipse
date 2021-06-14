use kino; 

SELECT v_visningnr, v_filmnr, f_filmnavn, v_pris, v_dato, v_starttid, concat(v_dato,' ', v_starttid) AS LagtSammen, NOW() - INTERVAL 1 HOUR
FROM tblvisning, tblbillett, tblplass, tblfilm
WHERE v_visningnr = b_visningsnr
	AND v_kinosalnr = p_kinosalnr
			AND v_dato >= CURDATE()
				AND f_filmnr = v_filmnr
GROUP BY v_visningnr, v_filmnr, f_filmnavn, v_pris, v_dato;

select concat(v_dato, ' ', v_starttid) as timestampDemo from tblvisning;

SELECT DATE_FORMAT(NOW()-INTERVAL 1 HOUR, "%H:%i:%s");

SELECT DATEDIFF(minute, starttime, endtime);

SELECT DATEADD(HOUR, -1, DATE_FORMAT(NOW(), "%H %i %s")) AS DateAdd;

SELECT DATEDIFF(HOUR,  DATEADD(HOUR, -1, '2013-03-13 00:00:00.000'), NOW());
    
SELECT *
FROM tbllogin;
    
SELECT *
FROM tblvisning;

SELECT *
FROM tblbillett;

SELECT *
FROM tblplass;