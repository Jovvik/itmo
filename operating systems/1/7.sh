#!/usr/bin/env bash

grep -Eraoh "[[:alnum:]!#$%&'*+-/=?^_\`{|}~]+@[[:alnum:]-]+\.[[:alnum:]-]+" /etc 2>/dev/null > emails.lst
exit 0