select sl.dat_event
  as dat_event, to_char(sl.dat_event, 'dd.MM.yyyy HH:mm:ss')
  as view_dat_event, sl.event
from slej_log sl
where dat_event between to_date('01.01.2016', 'dd.MM.yyyy') and to_date('ff.ff.ffff', 'dd.MM.yyyy')+1
order by dat_event desc