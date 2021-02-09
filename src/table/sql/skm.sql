select skm.hid, skm.dat_create
  as dat_create, to_char(skm.dat_create, 'dd.MM.yyyy hh24:mi:ss')
  as view_dat_create, to_char(skm.dat_send, 'dd.MM.yyyy hh24:mi:ss')
  as dat_send, skm.tema, skm.acc, skm.mailto, skm.hid,
case when skm.msg
  is null then '' else skm.file_name
  end as file_name
from slej_kont_mail skm
  where dat_create between to_date('ss.ss.ssss', 'dd.MM.yyyy')
  and to_date('ff.ff.ffff', 'dd.MM.yyyy')+1
  order by dat_create desc