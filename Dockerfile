FROM gradle:6.6.1-jdk11 AS build
COPY --chown=gradle:gradle . /usr/guia-bolso-transacao-api/src
WORKDIR /usr/guia-bolso-transacao-api/src
RUN gradle build
CMD ["gradle", "bootRun"]