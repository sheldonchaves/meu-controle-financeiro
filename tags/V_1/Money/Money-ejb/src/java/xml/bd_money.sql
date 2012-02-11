DROP TABLE IF EXISTS `guilherm_money`.`money_usuario`;
CREATE TABLE  `guilherm_money`.`money_usuario` (
  `id` bigint(20) NOT NULL auto_increment,
  `ds_email` varchar(100) NOT NULL,
  `ds_first_name` varchar(30) NOT NULL,
  `ds_last_name` varchar(30) NOT NULL,
  `ds_login` varchar(10) NOT NULL,
  `ds_password` varchar(50) NOT NULL,
  `fk_conjuge` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `ds_email` (`ds_email`),
  UNIQUE KEY `ds_login` (`ds_login`),
  KEY `FK_money_usuario_fk_conjuge` (`fk_conjuge`),
  CONSTRAINT `FK_money_usuario_fk_conjuge` FOREIGN KEY (`fk_conjuge`) REFERENCES `money_usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `guilherm_money`.`money_role`;
CREATE TABLE  `guilherm_money`.`money_role` (
  `id` int(11) NOT NULL auto_increment,
  `ds_desc` varchar(255) NOT NULL,
  `ds_name` varchar(15) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `ds_name` (`ds_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `guilherm_money`.`money_users_groups`;
CREATE TABLE  `guilherm_money`.`money_users_groups` (
  `fk_user_id` bigint(20) NOT NULL,
  `fk_role_id` int(11) NOT NULL,
  PRIMARY KEY  (`fk_user_id`,`fk_role_id`),
  KEY `FK_money_users_groups_fk_role_id` (`fk_role_id`),
  CONSTRAINT `FK_money_users_groups_fk_role_id` FOREIGN KEY (`fk_role_id`) REFERENCES `money_role` (`id`),
  CONSTRAINT `FK_money_users_groups_fk_user_id` FOREIGN KEY (`fk_user_id`) REFERENCES `money_usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `guilherm_money`.`money_conta_bancaria`;
CREATE TABLE  `guilherm_money`.`money_conta_bancaria` (
  `id` bigint(20) NOT NULL auto_increment,
  `ds_conta` varchar(100) NOT NULL,
  `vl_saldo` double NOT NULL,
  `fl_status` tinyint(1) NOT NULL default '0',
  `en_tipo` varchar(255) NOT NULL,
  `fk_user_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `uk_nomeconta_tipoconta` (`ds_conta`,`en_tipo`),
  KEY `FK_money_conta_bancaria_fk_user_id` (`fk_user_id`),
  CONSTRAINT `FK_money_conta_bancaria_fk_user_id` FOREIGN KEY (`fk_user_id`) REFERENCES `money_usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `guilherm_money`.`money_detalhe_movimentacao`;
CREATE TABLE  `guilherm_money`.`money_detalhe_movimentacao` (
  `id` bigint(20) NOT NULL auto_increment,
  `fl_ativo` tinyint(1) NOT NULL default '0',
  `ds_detalhe` varchar(100) NOT NULL,
  `en_tipo_movimentacao` varchar(255) NOT NULL,
  `fk_usuario` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `uk_detalhe_usuario` (`ds_detalhe`,`fk_usuario`),
  KEY `FK_money_detalhe_movimentacao_fk_usuario` (`fk_usuario`),
  CONSTRAINT `FK_money_detalhe_movimentacao_fk_usuario` FOREIGN KEY (`fk_usuario`) REFERENCES `money_usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `guilherm_money`.`money_lembrete_contas`;
CREATE TABLE  `guilherm_money`.`money_lembrete_contas` (
  `id` int(11) NOT NULL auto_increment,
  `nm_dias_vencimento` int(11) NOT NULL,
  `ds_email_contato` varchar(255) NOT NULL,
  `fl_status_aviso` tinyint(1) NOT NULL default '0',
  `fk_user_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK_money_lembrete_contas_fk_user_id` (`fk_user_id`),
  CONSTRAINT `FK_money_lembrete_contas_fk_user_id` FOREIGN KEY (`fk_user_id`) REFERENCES `money_usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `guilherm_money`.`money_receita_divida`;
CREATE TABLE  `guilherm_money`.`money_receita_divida` (
  `id` bigint(20) NOT NULL auto_increment,
  `dt_vencimento` date NOT NULL,
  `nm_identificador` varchar(50) default NULL,
  `vl_juros` double NOT NULL,
  `ds_observacao` varchar(255) default NULL,
  `nm_parcela_atual` int(11) NOT NULL,
  `nm_parcela_total` int(11) NOT NULL,
  `en_status_pgto` varchar(255) NOT NULL,
  `en_tipo_movimentacao` varchar(255) NOT NULL,
  `vl_valor` double NOT NULL,
  `fk_detalhe_movimentacao_id` bigint(20) NOT NULL,
  `fk_usuario_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK_money_receita_divida_fk_detalhe_movimentacao_id` (`fk_detalhe_movimentacao_id`),
  KEY `FK_money_receita_divida_fk_usuario_id` (`fk_usuario_id`),
  CONSTRAINT `FK_money_receita_divida_fk_detalhe_movimentacao_id` FOREIGN KEY (`fk_detalhe_movimentacao_id`) REFERENCES `money_detalhe_movimentacao` (`id`),
  CONSTRAINT `FK_money_receita_divida_fk_usuario_id` FOREIGN KEY (`fk_usuario_id`) REFERENCES `money_usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `guilherm_money`.`money_movimentacao_financeira`;
CREATE TABLE  `guilherm_money`.`money_movimentacao_financeira` (
  `id` bigint(20) NOT NULL auto_increment,
  `dt_movimentacao` date NOT NULL,
  `vl_saldo_anterior` double NOT NULL,
  `vl_saldo_posterior` double NOT NULL,
  `vl_saldo_transferida_anterior` double default NULL,
  `vl_saldo_transferida_posterior` double default NULL,
  `fk_conta_bancaria_debitada_id` bigint(20) NOT NULL,
  `fk_conta_bancaria_transferida_id` bigint(20) default NULL,
  `fk_receita_divida_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `mnymvimentacaofinanceirafkcntbancariatransferidaid` (`fk_conta_bancaria_transferida_id`),
  KEY `money_movimentacao_financeira_fk_receita_divida_id` (`fk_receita_divida_id`),
  KEY `mnymovimentacaofinanceirafkcontabancariadebitadaid` (`fk_conta_bancaria_debitada_id`),
  CONSTRAINT `mnymovimentacaofinanceirafkcontabancariadebitadaid` FOREIGN KEY (`fk_conta_bancaria_debitada_id`) REFERENCES `money_conta_bancaria` (`id`),
  CONSTRAINT `mnymvimentacaofinanceirafkcntbancariatransferidaid` FOREIGN KEY (`fk_conta_bancaria_transferida_id`) REFERENCES `money_conta_bancaria` (`id`),
  CONSTRAINT `money_movimentacao_financeira_fk_receita_divida_id` FOREIGN KEY (`fk_receita_divida_id`) REFERENCES `money_receita_divida` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;