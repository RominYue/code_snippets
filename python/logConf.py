#coding=utf8
'''
Created on Feb 14, 2016

@author: myue
'''

import os
from logging import config

DEBUG = True
LOG_PATH = os.getcwd()

LOGGING = {
    'version': 1,
    'disable_existing_loggers': False,
    'formatters': {
        'simple': {
            'format': '%(levelname)s %(asctime)s %(process)d %(message)s'
        },
        'detail': {
            'format': '%(levelname)s %(asctime)s %(process)d ' +
                      '[%(module)s.%(funcName)s line:%(lineno)d] %(message)s',
        },
    },
    'handlers': {
        'console': {
            'level': 'DEBUG',
            'class': 'logging.StreamHandler',
            'formatter': 'simple'
        },
        'file': {
            'level': 'INFO',
            'formatter': 'simple',
            'class': 'logging.handlers.WatchedFileHandler',
            'filename': LOG_PATH + '/info.log',
        },
        'err_file': {
            'level': 'WARN',
            'formatter': 'detail',
            'class': 'logging.handlers.WatchedFileHandler',
            'filename': LOG_PATH + '/error.log',
        },
        'perf': {
            'level': 'INFO',
            'formatter': 'simple',
            'class': 'logging.handlers.WatchedFileHandler',
            'filename': LOG_PATH + '/perf.log',
        },
        'track': {
            'level': 'INFO',
            'formatter': 'simple',
            'class': 'logging.handlers.WatchedFileHandler',
            'filename': LOG_PATH + '/track.log',
        }
    },
    'loggers': {
        'test': {
            'handlers': ['console', 'file', 'err_file'] if DEBUG else
                        ['file', 'err_file'],
            'level': 'DEBUG',
            'propagate': True,
        },
        'perf': {
            'handlers': ['console','perf'],
            'level': 'DEBUG',
            'propagate': True,
        },
        'track': {
            'handlers': ['track'],
            'level': 'DEBUG',
            'propagate': True,
        }
    }
}

config.dictConfig(LOGGING)
