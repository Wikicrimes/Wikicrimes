-------
-- Stored procedure para reinicializar os contadores das estatisticas sem perder dados
-------
CREATE DEFINER=`root`@`localhost` PROCEDURE `resetEstatisticas`()
BEGIN
  UPDATE wikicrimes.tb_eci_estatistica_cidade
     SET ECI_QTD_USUARIOS = 0,
         ECI_QTD_CRIMES = 0,
         ECI_QTD_ROUBOS = 0,
         ECI_QTD_ROUBO_PESSOA = 0,
         ECI_QTD_ROUBO_PROPRIEDADE = 0,
         ECI_QTD_TENTATIVA_ROUBO_PESSOA = 0,
         ECI_QTD_TENTATIVA_ROUBO_PROPRIEDADE = 0,
         ECI_QTD_FURTO = 0,
         ECI_QTD_FURTO_PESSOA = 0,
         ECI_QTD_FURTO_PROPRIEDADE = 0,
         ECI_QTD_TENTATIVA_FURTO_PESSOA = 0,
         ECI_QTD_TENTATIVA_FURTO_PROPRIEDADE = 0,
         ECI_QTD_OUTROS = 0,
         ECI_QTD_OUTRO_RIXAS = 0,
         ECI_QTD_OUTRO_VIOLENCIA_DOMESTICA = 0,
         ECI_QTD_OUTRO_HOMICIDIO = 0,
         ECI_QTD_OUTRO_TENTATIVA_HOMICIDIO = 0,
         ECI_QTD_OUTRO_LATROCINIO = 0,
         ECI_QTD_TURNO_UM = 0,
         ECI_QTD_TURNO_DOIS = 0,
         ECI_QTD_TURNO_TRES = 0,
         ECI_QTD_TURNO_QUATRO = 0,
         ECI_QTD_TENTATIVA_ROUBO = 0,
         ECI_QTD_TENTATIVA_FURTO = 0,
         ECI_QTD_OUTRO_ABUSO_AUTORIDADE = 0
     WHERE ECI_IDESTATISTICA_CIDADE > 0;

     UPDATE wikicrimes.tb_ees_estatistica_estado
     SET EES_QTD_USUARIOS = 0,
     EES_QTD_CRIMES = 0,
     EES_QTD_ROUBOS = 0,
     EES_QTD_ROUBO_PESSOA = 0,
     EES_QTD_ROUBO_PROPRIEDADE = 0,
     EES_QTD_TENTATIVA_ROUBO_PESSOA = 0,
     EES_QTD_TENTATIVA_ROUBO_PROPRIEDADE = 0,
     EES_QTD_FURTO = 0,
     EES_QTD_FURTO_PESSOA = 0,
     EES_QTD_FURTO_PROPRIEDADE = 0,
     EES_QTD_TENTATIVA_FURTO_PESSOA = 0,
     EES_QTD_TENTATIVA_FURTO_PROPRIEDADE = 0,
     EES_QTD_OUTROS = 0,
     EES_QTD_OUTRO_RIXAS = 0,
     EES_QTD_OUTRO_VIOLENCIA_DOMESTICA = 0,
     EES_QTD_OUTRO_HOMICIDIO = 0,
     EES_QTD_OUTRO_TENTATIVA_HOMICIDIO = 0,
     EES_QTD_OUTRO_LATROCINIO = 0,
     EES_QTD_TURNO_UM = 0,
     EES_QTD_TURNO_DOIS = 0,
     EES_QTD_TURNO_TRES = 0,
     EES_QTD_TURNO_QUATRO = 0,
     EES_QTD_TENTATIVA_ROUBO = 0,
     EES_QTD_TENTATIVA_FURTO = 0,
     EES_QTD_OUTRO_ABUSO_AUTORIDADE = 0
     WHERE EES_IDESTATISTICA_ESTADO > 0;

     UPDATE wikicrimes.tb_epa_estatistica_pais
     SET EPA_QTD_CRIMES = 0,
     EPA_QTD_ROUBOS = 0,
     EPA_QTD_ROUBO_PESSOA = 0,
     EPA_QTD_ROUBO_PROPRIEDADE = 0,
     EPA_QTD_TENTATIVA_ROUBO_PESSOA = 0,
     EPA_QTD_TENTATIVA_ROUBO_PROPRIEDADE = 0,
     EPA_QTD_FURTO = 0,
     EPA_QTD_FURTO_PESSOA = 0,
     EPA_QTD_FURTO_PROPRIEDADE = 0,
     EPA_QTD_TENTATIVA_FURTO_PESSOA = 0,
     EPA_QTD_TENTATIVA_FURTO_PROPRIEDADE = 0,
     EPA_QTD_OUTROS = 0,
     EPA_QTD_OUTRO_RIXAS = 0,
     EPA_QTD_OUTRO_VIOLENCIA_DOMESTICA = 0,
     EPA_QTD_OUTRO_HOMICIDIO = 0,
     EPA_QTD_OUTRO_TENTATIVA_HOMICIDIO = 0,
     EPA_QTD_OUTRO_LATROCINIO = 0,
     EPA_QTD_TURNO_UM = 0,
     EPA_QTD_TURNO_DOIS = 0,
     EPA_QTD_TURNO_TRES = 0,
     EPA_QTD_TURNO_QUATRO = 0,
     EPA_QTD_TENTATIVA_ROUBO = 0,
     EPA_QTD_TENTATIVA_FURTO = 0,
     EPA_QTD_OUTRO_ABUSO_AUTORIDADE = 0
     WHERE EPA_IDESTATISTICA_PAIS > 0;

END
-------
-- Store Procedure para update de estatisticas
-------

CREATE DEFINER=`root`@`localhost` PROCEDURE `updateEstatisticas`(novoPais VARCHAR(255), novoEstado VARCHAR(255), novaCidade VARCHAR(255),
idTipoCrime INTEGER, idTipoVitima INTEGER, horario INTEGER)
BEGIN
 declare p integer default 0;
 declare codPais integer;
 declare e integer default 0;
 declare codEstado integer;
 declare c integer default 0;

 select count(*) into p from tb_epa_estatistica_pais where epa_sigla = novoPais;
 select count(*) into e from tb_ees_estatistica_estado where ees_sigla = novoEstado;
 select count(*) into c from tb_eci_estatistica_cidade where eci_nome = novaCidade;

 if p = 0 then
  insert into tb_epa_estatistica_pais (epa_nome,epa_sigla,epa_qtd_usuarios,epa_qtd_crimes) values ('', novoPais,0,1);
 else
  update tb_epa_estatistica_pais set epa_qtd_crimes = epa_qtd_crimes + 1 where epa_sigla = novoPais;
 end if;

 if e = 0 then
   select epa_idestatistica_pais into codPais from tb_epa_estatistica_pais where epa_sigla = novoPais;
   insert into tb_ees_estatistica_estado (ees_nome,ees_sigla,ees_qtd_usuarios,ees_qtd_crimes,epa_idestatistica_pais) values ('', novoEstado,0,1,codPais);
 else
  update tb_ees_estatistica_estado set ees_qtd_crimes = ees_qtd_crimes + 1 where ees_sigla = novoEstado;
 end if;

 if c = 0 then
   select ees_idestatistica_estado into codEstado from tb_ees_estatistica_estado where ees_sigla = novoEstado;
   insert into tb_eci_estatistica_cidade (eci_nome,eci_qtd_usuarios,eci_qtd_crimes,ees_idestatistica_estado) values (novaCidade,0,1,codEstado);
 else
  update tb_eci_estatistica_cidade set eci_qtd_crimes = eci_qtd_crimes + 1 where eci_nome = novaCidade;
 end if;

 case idTipoCrime
 when 4 then
 begin
  update tb_epa_estatistica_pais set epa_qtd_roubos = epa_qtd_roubos + 1 where epa_sigla = novoPais;
  update tb_ees_estatistica_estado set ees_qtd_roubos = ees_qtd_roubos + 1 where ees_sigla = novoEstado;
  update tb_eci_estatistica_cidade set eci_qtd_roubos = eci_qtd_roubos + 1 where eci_nome = novaCidade;
  if idTipoVitima = 1 then
   update tb_epa_estatistica_pais set epa_qtd_roubo_pessoa = epa_qtd_roubo_pessoa + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_roubo_pessoa = ees_qtd_roubo_pessoa + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_roubo_pessoa = eci_qtd_roubo_pessoa + 1 where eci_nome = novaCidade;
  elseif idTipoVitima = 2 then
   update tb_epa_estatistica_pais set epa_qtd_roubo_propriedade = epa_qtd_roubo_propriedade + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_roubo_propriedade = ees_qtd_roubo_propriedade + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_roubo_propriedade = eci_qtd_roubo_propriedade + 1 where eci_nome = novaCidade;
  end if;
 end;
 when 3 then
 begin
  update tb_epa_estatistica_pais set epa_qtd_furto = epa_qtd_furto + 1 where epa_sigla = novoPais;
  update tb_ees_estatistica_estado set ees_qtd_furto = ees_qtd_furto + 1 where ees_sigla = novoEstado;
  update tb_eci_estatistica_cidade set eci_qtd_furto = eci_qtd_furto + 1 where eci_nome = novaCidade;
  if idTipoVitima = 1 then
   update tb_epa_estatistica_pais set epa_qtd_furto_pessoa = epa_qtd_furto_pessoa + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_furto_pessoa = ees_qtd_furto_pessoa + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_furto_pessoa = eci_qtd_furto_pessoa + 1 where eci_nome = novaCidade;
  elseif idTipoVitima = 2 then
   update tb_epa_estatistica_pais set epa_qtd_furto_propriedade = epa_qtd_furto_propriedade + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_furto_propriedade = ees_qtd_furto_propriedade + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_furto_propriedade = eci_qtd_furto_propriedade + 1 where eci_nome = novaCidade;
  end if;
 end;
 when 5 then
 begin
  update tb_epa_estatistica_pais set epa_qtd_outros = epa_qtd_outros + 1 where epa_sigla = novoPais;
  update tb_ees_estatistica_estado set ees_qtd_outros = ees_qtd_outros + 1 where ees_sigla = novoEstado;
  update tb_eci_estatistica_cidade set eci_qtd_outros = eci_qtd_outros + 1 where eci_nome = novaCidade;
  if idTipoVitima = 3 then
   update tb_epa_estatistica_pais set epa_qtd_outro_rixas = epa_qtd_outro_rixas + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_outro_rixas = ees_qtd_outro_rixas + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_outro_rixas = eci_qtd_outro_rixas + 1 where eci_nome = novaCidade;
  elseif idTipoVitima = 4 then
   update tb_epa_estatistica_pais set epa_qtd_outro_violencia_domestica = epa_qtd_outro_violencia_domestica + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_outro_violencia_domestica = ees_qtd_outro_violencia_domestica + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_outro_violencia_domestica = eci_qtd_outro_violencia_domestica + 1 where eci_nome = novaCidade;
  elseif idTipoVitima = 5 then
   update tb_epa_estatistica_pais set epa_qtd_outro_abuso_autoridade = epa_qtd_outro_abuso_autoridade + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_outro_abuso_autoridade = ees_qtd_outro_abuso_autoridade + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_outro_abuso_autoridade = eci_qtd_outro_abuso_autoridade + 1 where eci_nome = novaCidade;
  elseif idTipoVitima = 6 then
   update tb_epa_estatistica_pais set epa_qtd_outro_homicidio = epa_qtd_outro_homicidio + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_outro_homicidio = ees_qtd_outro_homicidio + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_outro_homicidio = eci_qtd_outro_homicidio + 1 where eci_nome = novaCidade;
  elseif idTipoVitima = 7 then
   update tb_epa_estatistica_pais set epa_qtd_outro_tentativa_homicidio = epa_qtd_outro_tentativa_homicidio + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_outro_tentativa_homicidio = ees_qtd_outro_tentativa_homicidio + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_outro_tentativa_homicidio = eci_qtd_outro_tentativa_homicidio + 1 where eci_nome = novaCidade;
  elseif idTipoVitima = 8 then
   update tb_epa_estatistica_pais set epa_qtd_outro_latrocinio = epa_qtd_outro_latrocinio + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_outro_latrocinio = ees_qtd_outro_latrocinio + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_outro_latrocinio = eci_qtd_outro_latrocinio + 1 where eci_nome = novaCidade;
  end if;
 end;
 when 1 then
 begin
  update tb_epa_estatistica_pais set epa_qtd_tentativa_roubo = epa_qtd_tentativa_roubo + 1 where epa_sigla = novoPais;
  update tb_ees_estatistica_estado set ees_qtd_tentativa_roubo = ees_qtd_tentativa_roubo + 1 where ees_sigla = novoEstado;
  update tb_eci_estatistica_cidade set eci_qtd_tentativa_roubo = eci_qtd_tentativa_roubo + 1 where eci_nome = novaCidade;
  if idTipoVitima = 1 then
   update tb_epa_estatistica_pais set epa_qtd_tentativa_roubo_pessoa = epa_qtd_tentativa_roubo_pessoa + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_tentativa_roubo_pessoa = ees_qtd_tentativa_roubo_pessoa + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_tentativa_roubo_pessoa = eci_qtd_tentativa_roubo_pessoa + 1 where eci_nome = novaCidade;
  elseif idTipoVitima = 2 then
   update tb_epa_estatistica_pais set epa_qtd_tentativa_roubo_propriedade = epa_qtd_tentativa_roubo_propriedade + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_tentativa_roubo_propriedade = ees_qtd_tentativa_roubo_propriedade + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_tentativa_roubo_propriedade = eci_qtd_tentativa_roubo_propriedade + 1 where eci_nome = novaCidade;
  end if;
 end;
 when 2 then
 update tb_epa_estatistica_pais set epa_qtd_tentativa_furto = epa_qtd_tentativa_furto + 1 where epa_sigla = novoPais;
 update tb_ees_estatistica_estado set ees_qtd_tentativa_furto = ees_qtd_tentativa_furto + 1 where ees_sigla = novoEstado;
 update tb_eci_estatistica_cidade set eci_qtd_tentativa_furto = eci_qtd_tentativa_furto + 1 where eci_nome = novaCidade;
 begin
  if idTipoVitima = 1 then
   update tb_epa_estatistica_pais set epa_qtd_tentativa_furto_pessoa = epa_qtd_tentativa_furto_pessoa + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_tentativa_furto_pessoa = ees_qtd_tentativa_furto_pessoa + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_tentativa_furto_pessoa = eci_qtd_tentativa_furto_pessoa + 1 where eci_nome = novaCidade;
  elseif idTipoVitima = 2 then
   update tb_epa_estatistica_pais set epa_qtd_tentativa_furto_propriedade = epa_qtd_tentativa_furto_propriedade + 1 where epa_sigla = novoPais;
   update tb_ees_estatistica_estado set ees_qtd_tentativa_furto_propriedade = ees_qtd_tentativa_furto_propriedade + 1 where ees_sigla = novoEstado;
   update tb_eci_estatistica_cidade set eci_qtd_tentativa_furto_propriedade = eci_qtd_tentativa_furto_propriedade + 1 where eci_nome = novaCidade;
  end if;
 end;
 else
  begin
  end;
 end case;

 if horario < 7 then
  update tb_epa_estatistica_pais set epa_qtd_turno_um = epa_qtd_turno_um + 1 where epa_sigla = novoPais;
  update tb_ees_estatistica_estado set ees_qtd_turno_um = ees_qtd_turno_um + 1 where ees_sigla = novoEstado;
  update tb_eci_estatistica_cidade set eci_qtd_turno_um = eci_qtd_turno_um + 1 where eci_nome = novaCidade;
 elseif horario > 6 and horario < 13 then
  update tb_epa_estatistica_pais set epa_qtd_turno_dois = epa_qtd_turno_dois + 1 where epa_sigla = novoPais;
  update tb_ees_estatistica_estado set ees_qtd_turno_dois = ees_qtd_turno_dois + 1 where ees_sigla = novoEstado;
  update tb_eci_estatistica_cidade set eci_qtd_turno_dois = eci_qtd_turno_dois + 1 where eci_nome = novaCidade;
 elseif horario > 12 and horario < 19 then
  update tb_epa_estatistica_pais set epa_qtd_turno_tres = epa_qtd_turno_tres + 1 where epa_sigla = novoPais;
  update tb_ees_estatistica_estado set ees_qtd_turno_tres = ees_qtd_turno_tres + 1 where ees_sigla = novoEstado;
  update tb_eci_estatistica_cidade set eci_qtd_turno_tres = eci_qtd_turno_tres + 1 where eci_nome = novaCidade;
 else
  update tb_epa_estatistica_pais set epa_qtd_turno_quatro = epa_qtd_turno_quatro + 1 where epa_sigla = novoPais;
  update tb_ees_estatistica_estado set ees_qtd_turno_quatro = ees_qtd_turno_quatro + 1 where ees_sigla = novoEstado;
  update tb_eci_estatistica_cidade set eci_qtd_turno_quatro = eci_qtd_turno_quatro + 1 where eci_nome = novaCidade;
 end if;
END

--------
--- Trigger de Inserção
--------

create trigger estatisticasInsert before insert on tb_cri_crime
 for each row
begin
	if new.cri_pais is not null and new.cri_estado is not null and new.cri_cidade is not null and new.cri_pais <> '' and new.cri_estado <> '' and new.cri_cidade <> '' then
    call estatisticasUpdate(new.cri_pais,new.cri_estado,new.cri_cidade,new.tcr_idtipo_crime,new.tvi_idtipo_vitima,new.cri_horario);
	end if;
end
---------
-- Trigger de Update
-- Verifica se os campos pais, cidade e estado antigos são nulos, se os campos novos campos são diferente de nulo e nao são vazios
-- A verificação dos campos novos se deve a confirmações de crimes que ainda não tem os campos cidade, estado e pais
---------

create trigger estatisticasUpdate before update on tb_cri_crime
for each row
begin
	if old.cri_pais is null and old.cri_estado is null and old.cri_cidade is null and new.cri_pais is not null and new.cri_estado is not null and new.cri_cidade is not null and new.cri_pais <> '' and new.cri_estado <> '' and new.cri_cidade <> '' then
   		call estatisticasUpdate(new.cri_pais,new.cri_estado,new.cri_cidade,new.tcr_idtipo_crime,new.tvi_idtipo_vitima,new.cri_horario);
	end if;
end
----------
-- Store Procedure para calculo das estatisticas em batch
----------

CREATE DEFINER=`root`@`localhost` PROCEDURE `calcularEstatisticas`()
BEGIN
  DECLARE done INT DEFAULT 0;
  DECLARE pais VARCHAR(255);
  DECLARE estado VARCHAR(255);
  DECLARE cidade VARCHAR(255);
  DECLARE idTipoCrime,idTipoVitima,horario INT;
  DECLARE cur1 CURSOR FOR SELECT tcr_idtipo_crime,tvi_idtipo_vitima,cri_horario,cri_pais,cri_estado,cri_cidade FROM wikicrimes.tb_cri_crime;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

  OPEN cur1;

  REPEAT
    FETCH cur1 INTO idTipoCrime,idTipoVitima,horario,pais,estado,cidade;
    IF NOT done THEN
       IF pais IS NOT NULL AND estado IS NOT NULL AND cidade IS NOT NULL AND idTipoCrime IS NOT NULL AND idTipoVitima IS NOT NULL AND horario IS NOT NULL THEN
          call wikicrimes.updateEstatisticas(pais,estado,cidade,idTipoCrime,idTipoVitima,horario);
       END IF;
    END IF;
  UNTIL done END REPEAT;

  CLOSE cur1;

END

---------
-- Store procedure para zerar as tabelas de estatistica
---------

CREATE DEFINER=`root`@`localhost` PROCEDURE `zerarEstatisticas`()
BEGIN
  DELETE FROM wikicrimes.tb_eci_estatistica_cidade;
  DELETE FROM wikicrimes.tb_ees_estatistica_estado;
  DELETE FROM wikicrimes.tb_epa_estatistica_pais;
END

---------
-- update campos nulos para idtipo_vitima na tabela de crimes
---------

CREATE DEFINER=`root`@`localhost` PROCEDURE `updateTipoVitima`()
BEGIN
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=1 where tvi_idtipo_vitima is null and tlo_idtipo_local = 1;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=1 where tvi_idtipo_vitima is null and tlo_idtipo_local = 2;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=1 where tvi_idtipo_vitima is null and tlo_idtipo_local = 3;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=1 where tvi_idtipo_vitima is null and tlo_idtipo_local = 4;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=1 where tvi_idtipo_vitima is null and tlo_idtipo_local = 5;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=1 where tvi_idtipo_vitima is null and tlo_idtipo_local = 6;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=2 where tvi_idtipo_vitima is null and tlo_idtipo_local = 7;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=2 where tvi_idtipo_vitima is null and tlo_idtipo_local = 8;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=2 where tvi_idtipo_vitima is null and tlo_idtipo_local = 9;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=2 where tvi_idtipo_vitima is null and tlo_idtipo_local = 10;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=2 where tvi_idtipo_vitima is null and tlo_idtipo_local = 11;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=2 where tvi_idtipo_vitima is null and tlo_idtipo_local = 12;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=2 where tvi_idtipo_vitima is null and tlo_idtipo_local = 13;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=1 where tvi_idtipo_vitima is null and tlo_idtipo_local = 14;
     update wikicrimes.tb_cri_crime set tvi_idtipo_vitima=2 where tvi_idtipo_vitima is null and tlo_idtipo_local = 15;
END


--------
--Latitude e longitude dos estados
-------


update `tb_ees_estatistica_estado` set ees_latitude=51.923943, ees_longitude=7.668457 where ees_sigla = 'Nordrhein-Westfalen';
update `tb_ees_estatistica_estado` set ees_latitude=-9.331083, ees_longitude=-70.301514 where ees_sigla = 'AC';
update `tb_ees_estatistica_estado` set ees_latitude=-3.054628, ees_longitude=-60.951172 where ees_sigla = 'AM';
update `tb_ees_estatistica_estado` set ees_latitude=1.206333, ees_longitude=-61.853027 where ees_sigla = 'RR';
update `tb_ees_estatistica_estado` set ees_latitude=1.964984, ees_longitude=-52.382812 where ees_sigla = 'AP';
update `tb_ees_estatistica_estado` set ees_latitude=-1.845384, ees_longitude=-48.691406 where ees_sigla = 'PA';
update `tb_ees_estatistica_estado` set ees_latitude=-2.943041, ees_longitude=-45.263672 where ees_sigla = 'MA';
update `tb_ees_estatistica_estado` set ees_latitude=-7.689217, ees_longitude=-42.648926 where ees_sigla = 'PI';
update `tb_ees_estatistica_estado` set ees_latitude=-4.017699, ees_longitude=-39.353027 where ees_sigla = 'CE';
update `tb_ees_estatistica_estado` set ees_latitude=-5.615986, ees_longitude=-36.210937 where ees_sigla = 'RN';
update `tb_ees_estatistica_estado` set ees_latitude=-7.489983, ees_longitude=-36.782227 where ees_sigla = 'PB';
update `tb_ees_estatistica_estado` set ees_latitude=-8.287887, ees_longitude=-37.870117 where ees_sigla = 'PE';
update `tb_ees_estatistica_estado` set ees_latitude=-9.31899, ees_longitude=-36.694336 where ees_sigla = 'AL';
update `tb_ees_estatistica_estado` set ees_latitude=-10.811724, ees_longitude=-37.074738 where ees_sigla = 'SE';
update `tb_ees_estatistica_estado` set ees_latitude=-12.746969, ees_longitude=-42.011719 where ees_sigla = 'BA';
update `tb_ees_estatistica_estado` set ees_latitude=-18.958246, ees_longitude=-40.770264 where ees_sigla = 'ES';
update `tb_ees_estatistica_estado` set ees_latitude=-22.712856, ees_longitude=-43.209229 where ees_sigla = 'RJ';
update `tb_ees_estatistica_estado` set ees_latitude=-22.800308, ees_longitude=-50.120605 where ees_sigla = 'SP';
update `tb_ees_estatistica_estado` set ees_latitude=-25.048281, ees_longitude=-52.531128 where ees_sigla = 'PR';
update `tb_ees_estatistica_estado` set ees_latitude=-28.049342, ees_longitude=-51.086426 where ees_sigla = 'SC';
update `tb_ees_estatistica_estado` set ees_latitude=-29.22889, ees_longitude=-53.657227 where ees_sigla = 'RS';
update `tb_ees_estatistica_estado` set ees_latitude=-20.331878, ees_longitude=-54.536133 where ees_sigla = 'MS';
update `tb_ees_estatistica_estado` set ees_latitude=-12.01213, ees_longitude=-55.942383 where ees_sigla = 'MT';
update `tb_ees_estatistica_estado` set ees_latitude=-11.492408, ees_longitude=-63.28125 where ees_sigla = 'RO';
update `tb_ees_estatistica_estado` set ees_latitude=-15.578065, ees_longitude=-47.796021 where ees_sigla = 'DF';
update `tb_ees_estatistica_estado` set ees_latitude=-10.972198, ees_longitude=-48.55957 where ees_sigla = 'TO';
update `tb_ees_estatistica_estado` set ees_latitude=-15.834536, ees_longitude=-50.251465 where ees_sigla = 'GO';
