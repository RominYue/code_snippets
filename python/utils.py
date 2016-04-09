#-*-coding=utf8-*-
import argparse
import ConfigParser
import logging
import sys
import os

def cli_parse():
    parser = argparse.ArgumentParser("description='usage for query get'")
    parser.add_argument('-c', '--configfile', dest='conf', default='conf.cfg', help='config file name')
    parser.add_argument('-w', '--weight', dest='weight', required=True, help='weight value')
    args = parser.parse_args()
    return args

def read_config_file(configfile):
    config = dict()
    cf = ConfigParser.ConfigParser()
    #read config file
    cf.read(configfile)
    #get specific value
    config['hive'] = cf.get('system','hive')
    config['hadoop'] = cf.get('system','hadoop')
    config['threshold'] = cf.getint('parameters','threshold')

    return config

def exe_cmd(cmd):
    ret = os.system(cmd)
    if ret != 0:
        logging.error("--- %s failed ---" %cmd)
        sys.exit(1)

if __name__ == '__main__':
    args = cli_parse()
    config = read_config_file(args.conf)
    print config
