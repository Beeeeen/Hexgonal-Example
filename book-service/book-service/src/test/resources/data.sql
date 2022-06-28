-- 注意!!  '0000-00-00 00:00:00' 要替換成 null 不然H2會有異常

truncate table sample;
INSERT INTO `sample` (`id`, `uuid`, `foo`) VALUES
(1, 'UUID', 'hello');
