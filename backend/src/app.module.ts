import { Module } from '@nestjs/common';
import { PrismaProvider } from './prisma.service';
import { BlocksModule } from './block/block.module';
import { PostModule } from './post/post.module';
import { BlockFileModule } from './block-file/block-file.module';
import { PostApiModule } from './post-api/post-api.module';
import { FileModule } from './file/file.module';

@Module({
  imports: [BlocksModule, PostModule, BlockFileModule, PostApiModule, FileModule],
  controllers: [],
  providers: [PrismaProvider],
})
export class AppModule {}
