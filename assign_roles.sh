#!/bin/bash

# Check if input file is provided
if [ -z "$1" ]; then
  echo "Usage: $0 <input_file>"
  exit 1
fi

# Source the input file to load variables
source "$1"

# Check if required variables are set
if [ -z "$PROJECT_ID" ] || [ -z "$USER_EMAIL" ] || [ -z "${ROLES[*]}" ]; then
  echo "Error: PROJECT_ID, USER_EMAIL, or ROLES are not properly set in the input file."
  exit 1
fi

# Iterate over each role and assign it to the user
for ROLE in "${ROLES[@]}"
do
  echo "Assigning $ROLE to $USER_EMAIL in project $PROJECT_ID..."
  gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member=user:$USER_EMAIL \
    --role=$ROLE
done

echo "All roles have been assigned!"
