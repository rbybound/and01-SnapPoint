import { Module } from '@nestjs/common';
import { FileApiController } from '@/api/file-api/file-api.controller';
import { FileApiService } from '@/api/file-api/file-api.service';
import { PostApiController } from '@/api/post-api/post-api.controller';
import { PrismaService } from '@/common/prisma/prisma.service';
import { PrismaProvider } from '@/common/prisma/prisma.provider';
import { FileService } from '@/domain/file/file.service';
import { PostApiService } from '@/api/post-api/post-api.service';
import { ValidationService } from './validation/validation.service';
import { BlockService } from '@/domain/block/block.service';
import { PostService } from '@/domain/post/post.service';
import { TransformationService } from './transformation/transformation.service';
import { AuthController } from '@/api/auth/auth.controller';
import { JwtModule } from '@nestjs/jwt';
import { PassportModule } from '@nestjs/passport';
import { JwtStrategy } from '@/common/strategies/auth.jwt.strategy';
import { RefreshTokenService } from '@/domain/refresh-token/refresh-token.service';
import { UserService } from '@/domain/user/user.service';
import { AuthService } from './auth/auth.service';
import { RedisCacheModule } from '@/common/redis/redis-cache.module';
import { ClientsModule, Transport } from '@nestjs/microservices';
import { ConfigModule, ConfigService } from '@nestjs/config';

@Module({
  imports: [
    ClientsModule.registerAsync([
      {
        name: 'MEDIA_SERVICE',
        imports: [ConfigModule],
        useFactory: async (configService: ConfigService) => ({
          transport: Transport.RMQ,
          options: {
            urls: [configService.getOrThrow<string>('RMQ_HOST')],
            queue: configService.getOrThrow<string>('RMQ_MEDIA_QUEUE'),
            queueOptions: {
              durable: true,
            },
          },
        }),
        inject: [ConfigService],
      },
    ]),
    PassportModule.register({
      defaultStrategy: 'jwt',
      session: false,
    }),
    JwtModule.registerAsync({
      useFactory: async (configService: ConfigService) => ({
        secret: configService.getOrThrow('JWT_ACCESS_SECRET'),
        signOptions: {
          expiresIn: configService.getOrThrow('JWT_ACCESS_EXPIRATION_TIME'),
        },
      }),
      inject: [ConfigService],
    }),
    RedisCacheModule,
  ],
  controllers: [FileApiController, PostApiController, AuthController],
  providers: [
    PrismaService,
    PrismaProvider,
    FileApiService,
    PostApiService,
    ValidationService,
    FileService,
    BlockService,
    PostService,
    TransformationService,
    ConfigService,
    AuthService,
    UserService,
    JwtStrategy,
    RefreshTokenService,
  ],
})
export class ApiModule {}
