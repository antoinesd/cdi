#!/usr/bin/env bash

asciidoctor -o target/generated-docs/cdi-spec.html src/main/asciidoc/cdi-spec.asciidoc

bundle exec guard
