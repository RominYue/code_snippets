@echo off
@rem get backup time to save as filename
set save_date=%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%
set save_path=D:\Projects\wiki_backup\
set save_file=%save_path%wiki_%save_date%.7z

@rem tmp_sql is the path that stores mysql dump file
@rem tmp_dir is the path that stores whole \wamp\www\wiki files
set wiki_path=D:\Projects\RucWiki\wamp\www\wiki\*.*
set tmp_sql=%save_path%tmp\wiki_backup.sql
set tmp_dir=%save_path%tmp\wiki\

@rem mysqldump and 7z bin path and pscp bin path
set mysql_path=D:\Projects\RucWiki\wamp\bin\mysql\mysql5.6.17\bin\mysqldump.exe
set path_7z="C:\Program Files\7-Zip\7z.exe"
set path_pscp="C:\Program Files\pscp.exe"

xcopy %wiki_path% %tmp_dir% /s /e
%mysql_path% -uroot -ppassword table_name > %tmp_sql%
%path_7z% a %save_file% %tmp_sql% %tmp_dir%

@rem backup to other sever
%path_pscp% -l user -pw password -P port %save_file% xxxx@IP:your_path
%path_pscp% -l user -pw password -P port %save_file% xxxx@IP:your_path

@rem del tmp files
rd %tmp_dir% /s /q
del %tmp_sql% /q
